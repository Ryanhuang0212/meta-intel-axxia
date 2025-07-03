FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append  = "file://0001-cve-2015-3290-Disable-AVX-for-x86_64.patch"
