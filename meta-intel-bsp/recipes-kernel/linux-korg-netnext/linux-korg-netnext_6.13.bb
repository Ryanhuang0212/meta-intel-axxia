require recipes-kernel/linux/linux-intel.inc
require recipes-kernel/linux/linux-axxia.inc

FILESEXTRAPATHS:prepend = "${@oe.utils.conditional('KORG_NETNEXT_EXTRA_PATH', '', '', '${KORG_NETNEXT_EXTRA_PATH}:', d)}"

SRC_URI:prepend = "git://git.kernel.org/pub/scm/linux/kernel/git/netdev/net-next.git;protocol=https;name=machine;branch=${KBRANCH}"

KBRANCH = "main"
KMETA_BRANCH = "yocto-6.12"

LINUX_VERSION = "6.13.0"
LINUX_VERSION_EXTENSION = "-korg-netnext-${LINUX_KERNEL_TYPE}"

SRCREV_machine = "0ad9617c78acbc71373fb341a6f75d4012b01d69"
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

# Add in SCR_URI patches and fragments from external path (KORG_NETNEXT_EXTRA_PATH) if exists
KORG_NETNEXT_EXTRA_PATH ?= ""

python __anonymous() {
    import os, bb

    dir = d.getVar('KORG_NETNEXT_EXTRA_PATH', True)

    if dir and os.path.isdir(dir):
        patches = sorted([f for f in os.listdir(dir) if f.endswith('.patch')])
        configs = sorted([f for f in os.listdir(dir) if f.endswith('.cfg')])

        new_files = []

        for patch in patches:
            new_files.append("file://" + patch)

        for config in configs:
            new_files.append("file://" + config)

        if new_files:
            src_uri = d.getVar('SRC_URI', True) or ""
            d.setVar('SRC_URI', src_uri + " " + " ".join(new_files))
}
