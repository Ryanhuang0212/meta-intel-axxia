SUMMARY = "Intel RDK tools pre-built"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-or-later;md5=fed54355545ffd980b814dab4a3b312c"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = "file://rdk_user_src.tgz \
           file://eeupdate64e \
           file://iqvlinux.ko"

BB_STRICT_CHECKSUM = "0"

PACKAGES =+ "rdk-firmware"
ALLOW_EMPTY:rdk-firmware = "1"

RDEPENDS:${PN} += "bash"

INSANE_SKIP:${PN} += "already-stripped ldflags dev-so file-rdeps"


INSANE_SKIP:${PN} += "already-stripped ldflags dev-so"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"


do_compile() {
    :
}


do_install() {
    install -d ${D}/home/root/rdk_user_src
    install -d ${D}/home/root/eeupdate64e

    if [ -d "${WORKDIR}/rdk" ]; then
        cp -r ${WORKDIR}/rdk/* ${D}/home/root/rdk_user_src/
    elif [ -d "${WORKDIR}/rdk-tools-1.0" ]; then
        cp -r ${WORKDIR}/rdk-tools-1.0/* ${D}/home/root/rdk_user_src/
    else
        echo "Searching for source in WORKDIR..."
    fi

    if [ -d "${WORKDIR}/eeupdate64e" ]; then
        cp -r ${WORKDIR}/eeupdate64e/* ${D}/home/root/eeupdate64e/
        chmod +x ${D}/home/root/eeupdate64e/eeupdate64e
    fi

    install -d ${D}${nonarch_base_libdir}/modules
    [ -f ${WORKDIR}/iqvlinux.ko ] && install -m 0644 ${WORKDIR}/iqvlinux.ko ${D}${nonarch_base_libdir}/modules/iqvlinux.ko

    install -d ${D}${sysconfdir}/modules-load.d
    echo "iqvlinux" > ${D}${sysconfdir}/modules-load.d/iqvlinux.conf
}


FILES:${PN} = "/home/root/rdk_user_src/* \
               /home/root/eeupdate64e/* \
               ${nonarch_base_libdir}/modules/iqvlinux.ko \
               ${sysconfdir}/modules-load.d/iqvlinux.conf"
