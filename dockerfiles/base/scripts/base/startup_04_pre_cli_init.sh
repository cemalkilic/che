#!/bin/sh
# Copyright (c) 2016 Codenvy, S.A.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html

cli_pre_init() {
  :
}

cli_post_init() {
  :
}

cli_init() {
  CHE_HOST=$(eval "echo \$${CHE_PRODUCT_NAME}_HOST")
  CHE_PORT=$(eval "echo \$${CHE_PRODUCT_NAME}_PORT")

  if [[ "$(eval "echo \$${CHE_PRODUCT_NAME}_HOST")" = "" ]]; then
    info "Welcome to $CHE_FORMAL_PRODUCT_NAME!"
    info ""
    info "We did not auto-detect a valid HOST or IP address."
    info "Pass ${CHE_PRODUCT_NAME}_HOST with your hostname or IP address."
    info ""
    info "Rerun the CLI:"
    info "  docker run -it --rm -v /var/run/docker.sock:/var/run/docker.sock"
    info "                      -v <local-path>:${CHE_CONTAINER_ROOT}"
    info "                      -e ${CHE_PRODUCT_NAME}_HOST=<your-ip-or-host>"
    info "                         $CHE_IMAGE_FULLNAME $*"
    return 2;
  fi

  if is_initialized; then 
    CHE_HOST_LOCAL=$(get_value_of_var_from_env_file ${CHE_PRODUCT_NAME}_HOST)
    if [[ "${CHE_HOST}" != "${CHE_HOST_LOCAL}" ]] && 
       [[ "${CHE_HOST_SET_ON_COMMAND_LINE}" = "true" ]]; then
      warning "Command line '-e ${CHE_PRODUCT_NAME}_HOST=${CHE_HOST}' \
overriding '${CHE_PRODUCT_NAME}_HOST=${CHE_HOST_LOCAL}' in ${CHE_ENVIRONMENT_FILE}"
    elif [[ "${CHE_HOST_LOCAL}" != "${GLOBAL_HOST_IP}" ]]; then
      warning "${CHE_PRODUCT_NAME}_HOST=${CHE_HOST_LOCAL} is different from discovered IP \
(${GLOBAL_HOST_IP})"
      CHE_HOST=${CHE_HOST_LOCAL}
    fi

    CHE_PORT_LOCAL=$(get_value_of_var_from_env_file ${CHE_PRODUCT_NAME}_PORT)
    if [[ "${CHE_PORT}" != "${CHE_PORT_LOCAL}" ]] && 
       [[ "${CHE_PORT_SET_ON_COMMAND_LINE}" = "true" ]]; then
      warning "Command line '-e ${CHE_PRODUCT_NAME}_PORT=${CHE_PORT}' \
overriding '${CHE_PRODUCT_NAME}_PORT=${CHE_PORT_LOCAL}' in ${CHE_ENVIRONMENT_FILE}"
    elif [[ "${CHE_PORT_LOCAL}" != "${DEFAULT_CHE_PORT}" ]]; then
      warning "${CHE_PRODUCT_NAME}_PORT=${CHE_PORT_LOCAL} is different from default port \
(${DEFAULT_CHE_PORT})"
      CHE_PORT=${CHE_PORT_LOCAL}
    fi
  fi

  # Special function to perform special behaviors if you are running nightly version
  verify_nightly_accuracy

  # Do not perform a version compatibility check if running upgrade command.
  # The upgrade command has its own internal checks for version compatibility.
  if [[ "$@" == *"upgrade"* ]]; then
    verify_version_upgrade_compatibility
  elif ! is_fast; then
    verify_version_compatibility
  fi
}

verify_nightly_accuracy() {
  # Per request of the engineers, check to see if the locally cached nightly version is older
  # than the one stored on DockerHub.
  if is_nightly; then

    if ! is_fast && ! skip_nightly; then
      local CURRENT_DIGEST=$(docker images -q --no-trunc --digests ${CHE_IMAGE_FULLNAME})

      update_image $CHE_IMAGE_FULLNAME

      local NEW_DIGEST=$(docker images -q --no-trunc --digests ${CHE_IMAGE_FULLNAME})

      if [[ "${CURRENT_DIGEST}" != "${NEW_DIGEST}" ]]; then
        warning "Pulled new 'nightly' image - please rerun CLI"
        return 2;
      fi
    fi 
  fi
}

is_nightly() {
  if [[ $(get_image_version) = "nightly" ]]; then
    return 0
  else
    return 1
  fi
}

verify_version_compatibility() {

  ## If ! is_initialized, then the system hasn't been installed
  ## First, compare the CLI image version to what version was initialized in /config/*.ver.donotmodify
  ##      - If they match, good
  ##      - If they don't match and one is nightly, fail
  ##      - If they don't match, then if CLI is older fail with message to get proper CLI
  ##      - If they don't match, then if CLLI is newer fail with message to run upgrade first
  CHE_IMAGE_VERSION=$(get_image_version)

  # Only check for newer versions if not in offline mode and not nightly.
  if ! is_offline && ! is_nightly; then
    NEWER=$(compare_versions $CHE_IMAGE_VERSION)

    if [[ "${NEWER}" != "" ]]; then
      warning "Newer version '$NEWER' available"
    fi
  fi

  if is_initialized; then
    COMPARE_CLI_ENV=$(compare_cli_version_to_installed_version)
    INSTALLED_VERSION=$(get_installed_version)
    case "${COMPARE_CLI_ENV}" in
      "match")
      ;;
      "nightly")
        error ""
        error "Your CLI version '${CHE_IMAGE_FULLNAME}' does not match your installed version '$INSTALLED_VERSION' in ${DATA_MOUNT}."
        error ""
        error "The 'nightly' CLI is only compatible with 'nightly' installed versions."
        error "You may use 'nightly' with a separate ${CHE_FORMAL_PRODUCT_NAME} installation by providing a different ':/data' volume mount."
        error "You may not '${CHE_MINI_PRODUCT_NAME} upgrade' from 'nightly' to a numbered (tagged) version."
        error ""
        error "Run the CLI as '${CHE_IMAGE_NAME}:<version>' to install a tagged version."
        return 2
      ;;
      "install-less-cli")
        error ""
        error "Your CLI version '${CHE_IMAGE_FULLNAME}' is newer than your installed version '$INSTALLED_VERSION'."
        error ""
        error "Run '${CHE_IMAGE_FULLNAME} upgrade' to migrate your installation to '$CHE_IMAGE_VERSION'."
        error "Or, run the CLI with '${CHE_IMAGE_NAME}:$INSTALLED_VERSION' to match the CLI with your installed version."
        return 2
      ;;
      "cli-less-install")
        error ""
        error "Your CLI version '${CHE_IMAGE_FULLNAME}' is older than your installed version '$INSTALLED_VERSION'."
        error ""
        error "You cannot use an older CLI with a newer installation."
        error ""
        error "Run the CLI with '${CHE_IMAGE_NAME}:$INSTALLED_VERSION' to match the CLI with your existing installed version."
        return 2
      ;;
    esac
  fi
}

verify_version_upgrade_compatibility() {
  ## Two levels of checks
  ## First, compare the CLI image version to what the admin has configured in /config/.env file
  ##      - If they match, nothing to upgrade
  ##      - If they don't match and one is nightly, fail upgrade is not supported for nightly
  ##      - If they don't match, then if CLI is older fail with message that we do not support downgrade
  ##      - If they don't match, then if CLI is newer then good
  CHE_IMAGE_VERSION=$(get_image_version)

  if ! is_initialized || ! is_configured; then
    info "upgrade" "$CHE_MINI_PRODUCT_NAME is not installed or configured. Nothing to upgrade."
    return 2
  fi

  if is_initialized; then
    COMPARE_CLI_ENV=$(compare_cli_version_to_installed_version)
    CONFIGURED_VERSION=$(get_installed_version)

    case "${COMPARE_CLI_ENV}" in
      "match")
        error ""
        error "Your CLI version '${CHE_IMAGE_FULLNAME}' is identical to your installed version '$CONFIGURED_VERSION'."
        error ""
        error "Run '${CHE_IMAGE_NAME}:<version> upgrade' with a newer version to upgrade."
        error "View available versions with '${CHE_IMAGE_FULLNAME} version'."
        return 2
      ;;
      "nightly")
        error ""
        error "Your CLI version '${CHE_IMAGE_FULLNAME}' or installed version '$CONFIGURED_VERSION' is nightly."
        error ""
        error "You may not '${CHE_IMAGE_NAME} upgrade' from 'nightly' to a numbered (tagged) version."
        error "You can 'docker pull ${CHE_IMAGE_FULLNAME}' to get a newer nightly version."
        return 2
      ;;
      "install-less-cli")
      ;;
      "cli-less-install")
        error ""
        error "Your CLI version '${CHE_IMAGE_FULLNAME}' is older than your installed version '$CONFIGURED_VERSION'."
        error ""
        error "You cannot use '${CHE_IMAGE_NAME} upgrade' to downgrade versions."
        error ""
        error "Run '${CHE_IMAGE_NAME}:<version> upgrade' with a newer version to upgrade."
        error "View available versions with '${CHE_IMAGE_FULLNAME} version'."
        return 2
      ;;
    esac
  fi
}

get_installed_version() {
  if ! is_initialized; then
    echo "<not-configed>"
  else
    cat "${CHE_CONTAINER_INSTANCE}"/$CHE_VERSION_FILE
  fi
}

get_image_version() {
  echo "$CHE_IMAGE_VERSION"
}

# Check if a version is < than another version (provide like for example : version_lt 5.0 5.1)
version_lt() {
 test "$(printf '%s\n' "$@" | sort -V | head -n 1)" == "$1";
 return $?;
}

# return true if the provided version is an intermediate version (like a Milestone or a Release Candidate)
is_intermediate_version() {
  if [[ "$1" =~ .*-M.* ]]; then
     return 0
  fi
  if [[ "$1" =~ .*-RC.* ]]; then
     return 0
  fi
  return 1
}

compare_cli_version_to_installed_version() {
  IMAGE_VERSION=$(get_image_version)
  INSTALLED_VERSION=$(get_installed_version)

  # add -ZZ suffix if not intermediate version
  # So for example 5.0.0 is transformed into 5.0.0-ZZ and is greater than 5.0.0-M8
  IMAGE_VERSION_SUFFIXED=${IMAGE_VERSION}
  INSTALLED_VERSION_SUFFIXED=${INSTALLED_VERSION}
  if ! is_intermediate_version  ${IMAGE_VERSION}; then
    IMAGE_VERSION_SUFFIXED="${IMAGE_VERSION}-ZZ"
  fi
  if ! is_intermediate_version ${INSTALLED_VERSION}; then
    INSTALLED_VERSION_SUFFIXED="${INSTALLED_VERSION}-ZZ"
  fi

  if [[ "$INSTALLED_VERSION" = "$IMAGE_VERSION" ]]; then
    echo "match"
  elif [ "$INSTALLED_VERSION" = "nightly" ] ||
       [ "$IMAGE_VERSION" = "nightly" ]; then
    echo "nightly"
  elif version_lt $INSTALLED_VERSION_SUFFIXED $IMAGE_VERSION_SUFFIXED; then
    echo "install-less-cli"
  else
    echo "cli-less-install"
  fi
}

is_initialized() {
  if [[ -d "${CHE_CONTAINER_INSTANCE}" ]] && \
     [[ -f "${CHE_CONTAINER_INSTANCE}"/$CHE_VERSION_FILE ]] && \
     [[ -f "${REFERENCE_CONTAINER_ENVIRONMENT_FILE}" ]]; then
    return 0
  else
    return 1
  fi
}

is_configured() {
  if [[ -d "${CHE_CONTAINER_INSTANCE}/config" ]] && \
     [[ -f "${CHE_CONTAINER_INSTANCE}/${CHE_VERSION_FILE}" ]]; then
    return 0
  else
    return 1
  fi
}

update_image() {
  if [ "${1}" == "--force" ]; then
    shift
    info "download" "Removing image $1"
    log "docker rmi -f $1 >> \"${LOGS}\""
    docker rmi -f $1 >> "${LOGS}" 2>&1 || true
  fi

  if [ "${1}" == "--pull" ]; then
    shift
  fi

  info "download" "Pulling image $1"
  text "\n"
  log "docker pull $1 >> \"${LOGS}\" 2>&1"
  TEST=""
  docker pull $1 || TEST=$?
  if [ "$TEST" = "1" ]; then
    error "Image $1 unavailable. Not on dockerhub or built locally."
    return 2;
  fi
  text "\n"
}

# Input is a variable that would exist in the che.env file such as CHE_HOST.
# We then lookup the vlaue of this variable and return it
get_value_of_var_from_env_file() {
  local LOOKUP_LOCAL=$(docker_run --env-file="${REFERENCE_CONTAINER_ENVIRONMENT_FILE}" \
                                ${BOOTSTRAP_IMAGE_ALPINE} sh -c "echo \$$1")
  echo $LOOKUP_LOCAL
}
