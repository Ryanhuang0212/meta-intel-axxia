IMAGE_NAME_SUFFIX:prepend = "${@oe.utils.conditional('RELEASE_VERSION', 'default', '', '--${RELEASE_VERSION}', d)}"
TOOLCHAIN_OUTPUTNAME = "${SDK_NAME}-toolchain--${SDK_VERSION}"
TOOLCHAINEXT_OUTPUTNAME = "${SDK_NAME}-toolchain-ext--${SDK_VERSION}"

GLIBC_GENERATE_LOCALES = "en_US.UTF-8"
IMAGE_LINGUAS = "en-us"
