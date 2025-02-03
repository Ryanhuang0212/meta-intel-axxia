require recipes-kernel/linux/linux-intel.inc
require recipes-kernel/linux/linux-axxia.inc

SRC_URI:prepend = "git://git.kernel.org/pub/scm/linux/kernel/git/netdev/net-next.git;protocol=https;name=machine;branch=${KBRANCH}"

KBRANCH = "main"
KMETA_BRANCH = "yocto-6.12"

LINUX_VERSION = "6.12.0"
LINUX_VERSION_EXTENSION = "-korg-netnext-${LINUX_KERNEL_TYPE}"

SRCREV_machine = "adc218676eef25575469234709c2d87185ca223a"
SRCREV_meta = "8f57fada9d056588228a27f6eaaaed9e176cd6a5"

COMMON_PATCHES = " \
"

SNR_PATCHES = " \
"

GRR_PATCHES = " \
"

PMR_PATCHES = " \
"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc features/security/security.scc"
