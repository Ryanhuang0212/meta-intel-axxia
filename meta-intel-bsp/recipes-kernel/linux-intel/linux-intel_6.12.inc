LINUX_VERSION = "6.12.22"
SRCREV_machine = "bc99adcec0b1c638635edfb8c917fbe25589e3a7"
SRCREV_meta = "702c2b220c24ba42deb79a2d3c40488a698bd77b"

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
