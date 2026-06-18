SUMMARY = "Intel RDK tools pre-built"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-or-later;md5=fed54355545ffd980b814dab4a3b312c"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI = "file://rdk_user_src.tgz            file://eeupdate64e            file://iqvlinux.ko"

BB_STRICT_CHECKSUM = "0"
S = "${WORKDIR}/rdk"
INSANE_SKIP:${PN} = "already-stripped ldflags dev-deps dev-so file-rdeps"

PACKAGES =+ "rdk-firmware"
ALLOW_EMPTY:rdk-firmware = "1"

do_compile() {
    :
}

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${libdir}
    install -d ${D}${includedir}
    install -d ${D}${sysconfdir}
    install -d ${D}${nonarch_base_libdir}/modules
    install -d ${D}${sysconfdir}/modules-load.d
    install -d ${D}${nonarch_base_libdir}/firmware/intel

    # 找出真實解壓縮後的路徑 (因為手動打包時多了一層 rdk)
    RDK_INSTALL_DIR="${S}/rdk/install"
    if [ ! -d "$RDK_INSTALL_DIR" ]; then
        RDK_INSTALL_DIR="${S}/install"
    fi

    # 複製原廠編譯好的 bin, lib, include, etc
    if [ -d "$RDK_INSTALL_DIR/bin" ]; then
        cp -r $RDK_INSTALL_DIR/bin/* ${D}${bindir}/
    fi
    if [ -d "$RDK_INSTALL_DIR/lib" ]; then
        cp -r $RDK_INSTALL_DIR/lib/* ${D}${libdir}/
    fi
    if [ -d "$RDK_INSTALL_DIR/include" ]; then
        cp -r $RDK_INSTALL_DIR/include/* ${D}${includedir}/
        rm -f ${D}${includedir}/Makefile
    fi
    if [ -d "$RDK_INSTALL_DIR/etc" ]; then
        cp -r $RDK_INSTALL_DIR/etc/* ${D}${sysconfdir}/
    fi

    if [ -d "$RDK_INSTALL_DIR/lib/firmware/intel" ]; then
        cp -r $RDK_INSTALL_DIR/lib/firmware/intel/* ${D}${nonarch_base_libdir}/firmware/intel/ 2>/dev/null || :
    fi

    # 複製額外的工具
    [ -f ${WORKDIR}/eeupdate64e ] && install -m 0755 ${WORKDIR}/eeupdate64e ${D}${bindir}/
    [ -f ${WORKDIR}/eltt2 ] && install -m 0755 ${WORKDIR}/eltt2 ${D}${bindir}/
    [ -f ${WORKDIR}/iqvlinux.ko ] && install -m 0644 ${WORKDIR}/iqvlinux.ko ${D}${nonarch_base_libdir}/modules/iqvlinux.ko
    
    echo "iqvlinux" > ${D}${sysconfdir}/modules-load.d/iqvlinux.conf
}

FILES:rdk-firmware = "${nonarch_base_libdir}/firmware"
FILES:${PN} = "${bindir} ${libdir} ${sysconfdir} ${nonarch_base_libdir}/modules/iqvlinux.ko ${sysconfdir}/modules-load.d/iqvlinux.conf"
FILES:${PN} += "/lib /lib/modules /lib/modules/*"
FILES:${PN}-dev = "${includedir}"
