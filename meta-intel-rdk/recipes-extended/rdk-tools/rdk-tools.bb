SUMMARY = "Intel RDK userspace tools"
DESCRIPTION = "Intel RDK package containing all userspace (API and CLI) sources."
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-or-later;md5=fed54355545ffd980b814dab4a3b312c"

USE_RDK_REPO ?= "false"
RDK_REPO ?= ""
RDK_REPO_REV ?= ""
RDK_REPO_SRC_URI ?= "git://${@d.getVar('RDK_REPO').replace('https://','')};protocol=https;nobranch=1"
SRCREV = "${RDK_REPO_REV}"

FILESEXTRAPATHS:prepend := "${RDK_LAYER_PATH}/downloads:"
RDK_TOOLS_ARCHIVE ?= "file://rdk_user_src.tar.xz"
BB_STRICT_CHECKSUM = "0"

SRC_URI = "${@oe.utils.conditional('USE_RDK_REPO', 'false', '${RDK_TOOLS_ARCHIVE}', '${RDK_REPO_SRC_URI}', d)}"

RDK_TOOLS_VERSION ?= "${@oe.utils.conditional('USE_RDK_REPO', 'false', \
	'unknown_release_info', 'git_${RDK_REPO_REV}', d)}"
PR = "${RDK_TOOLS_VERSION}"

DEPENDS = "virtual/kernel libnl libpcap openssl rsync-native thrift meson-native ninja-native \
	   ${@oe.utils.conditional('RDK_LTTNG_ENABLE', 'true', 'lttng-ust lttng-tools', '', d)}"

RDEPENDS:${PN} += "${@oe.utils.conditional('RDK_LTTNG_ENABLE', 'true', 'lttng-ust lttng-tools', '', d)}"

S = "${@oe.utils.conditional('USE_RDK_REPO', 'false', "${WORKDIR}/rdk/rdk", "${WORKDIR}/git/rdk", d)}"

inherit autotools pkgconfig

TARGET_CC_ARCH += "${SELECTED_OPTIMIZATION}"

export SDKTARGETSYSROOT = "${STAGING_DIR_HOST}"
export OECORE_NATIVE_SYSROOT = "${STAGING_DIR_NATIVE}"

export IES_ENABLE_SHM ??= "true"
export RDK_LTTNG_ENABLE ??= "true"
export LTTNG_ROOT = "${STAGING_DIR_HOST}${prefix}"

IES_EXTRA_FLAGS = "host_alias=${HOST_SYS}"

REMOVE_LIBTOOL_LA = "0"

CXXFLAGS += " -I${SYSROOT}/usr/kernel-headers/include/klm "

do_compile[cleandirs] = "${S}/install"

do_compile () {
	cd ${S}
	oe_runmake cpk-ae-lib netd-lib
	rm -rf ${S}/user_modules/ies-api/build
	oe_runmake ${IES_EXTRA_FLAGS} ies_api_install
	case "${MACHINE}" in
            "intel-axxia-snr") oe_runmake -j1 qat_lib nura ;;
            "intel-axxia-grr") oe_runmake -j1 qat_lib ;;
        esac
	oe_runmake install cli
}

do_install () {
	oe_runmake -C ${S} install CC="${CC}" CXX="${CXX}" LD="${CC}" AR="${AR}"

	install -d ${D}${bindir} ${D}${libdir}
	install -d ${D}${includedir} ${D}${sysconfdir}
	cp -r ${S}/install/bin/* ${D}${bindir}
	cp -r ${S}/install/lib/* ${D}${libdir}
	cp -r ${S}/install/include/* ${D}${includedir}
	cp -r ${S}/install/etc/* ${D}${sysconfdir}
	rm -f ${D}${includedir}/Makefile

	if [ -d ${S}/install/lib/firmware/intel ]; then
		install -d ${D}${nonarch_base_libdir}/firmware/intel
		cp -r ${S}/install/lib/firmware/intel/* \
		${D}${nonarch_base_libdir}/firmware/intel 2>/dev/null || :
	fi

	cp -r ${S}/user_modules/ies-api/lib/* ${D}${libdir} 2>/dev/null || :

	sed -i "s#libdir=.*#libdir='${libdir}'#g" \
		${D}${libdir}/*.la 2>/dev/null || :

	sed -i "s#${S}/user_modules/ies-api/lib#${libdir}#g" ${D}${bindir}/*cli

	sed -i 's#prefix=.*#prefix=${prefix}#' \
		${D}${libdir}/pkgconfig/*.pc 2>/dev/null || :

	# Break hardlinks before chown to avoid Pseudo SIGABRT
	find ${D} -type f | while IFS= read -r f; do
		if [ $(stat -c %h "$f") -gt 1 ]; then
			cp "$f" "${f}.newino"
			mv "${f}.newino" "$f"
		fi
	done
	chown -R root:root ${D}
}

do_install:append () {
	# Install iesserver binary
	if [ -f ${S}/user_modules/ies-api/build/iesserver ]; then
		install -m 0755 ${S}/user_modules/ies-api/build/iesserver ${D}${bindir}/
	fi

	# Install user_modules (exclude .so to avoid multiple shlib providers)
	install -d ${D}/home/root/user_modules
	find ${S}/user_modules -mindepth 1 -type d | while IFS= read -r d; do
		install -d "${D}/home/root/user_modules/${d#${S}/user_modules/}"
	done
	find ${S}/user_modules -type f ! -name "*.so" ! -name "*.so.*" | while IFS= read -r f; do
		rel="${f#${S}/user_modules/}"
		cp "$f" "${D}/home/root/user_modules/$rel"
	done
}

PACKAGES += "rdk-firmware"

FILES:rdk-firmware = " ${nonarch_base_libdir}/firmware"
ALLOW_EMPTY:rdk-firmware = "1"

FILES:${PN}-dev = " ${includedir} \
	${libdir}/libies*.so \
	${libdir}/*.la"

FILES:${PN} = " ${bindir} ${sysconfdir} ${libdir} /home/root/user_modules"

INSANE_SKIP:${PN} = "already-stripped ldflags dev-deps dev-so staticdev arch buildpaths host-user-contaminated"

BBCLASSEXTEND = "native nativesdk"
