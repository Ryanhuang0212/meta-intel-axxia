require linux-korg-netnext_6.12.bb

SRC_URI:append = " file://debug.scc"

LINUX_VERSION_EXTENSION = "-korg-netnext-debug-${LINUX_KERNEL_TYPE}"

INSANE_SKIP += "buildpaths"
