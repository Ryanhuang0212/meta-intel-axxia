require linux-intel-private_6.12.bb

LINUX_VERSION_EXTENSION = "-intel-private-debug-${LINUX_KERNEL_TYPE}"

KBUILD_DEFCONFIG:intel-axxia-pmr = "pmr_debug_defconfig"

INSANE_SKIP += "buildpaths"
