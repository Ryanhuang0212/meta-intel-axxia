LINUX_VERSION = "6.12.34"
SRCREV_machine = "9d4793b1384e83c55cc689d0f0b82c2fc4d488ba"
SRCREV_meta = "bdce1d136816ca2538d5f1b1f699e8abafb0d1a9"

COMMON_PATCHES = " \
file://0001-6.12-lib-build_OID_registry-fix-reproducibility-issues.patch \
file://0002-drivers-watchdog-Ignore-No-Reboot-Bit.patch \
file://0003-clocksource-Add-option-to-force-acpi_pm-as-clocksour.patch \
"

SNR_PATCHES = " \
"

GRR_PATCHES = " \
file://0001-platform-msi-Add-device-MSI-infrastructure.patch \
file://0002-driver-core-auxiliary-bus-Add-Config-Option.patch \
"

PMR_PATCHES = " \
file://0001-platform-msi-Add-device-MSI-infrastructure.patch \
file://0002-driver-core-auxiliary-bus-Add-Config-Option.patch \
"
