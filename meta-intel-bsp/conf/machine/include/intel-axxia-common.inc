EFI_PROVIDER ?= "grub-efi"

require conf/machine/include/meta-intel.inc
require conf/machine/include/intel-common-pkgarch.inc

MACHINE_FEATURES += "pcbios efi"
MACHINE_FEATURES += "intel-ucode ext3"

KARCH = "x86_64"
KERNEL_IMAGETYPE = "bzImage"
KEEPUIMAGE = "no"

IMAGE_FSTYPES += " ext2"
IMAGE_FSTYPES += " tar.gz"
IMAGE_FSTYPES += " iso"

IMAGE_FSTYPES += "wic"
WKS_FILE = "axxia-bootdisk-microcode.wks.in"
WKS_FILE_DEPENDS:append = " intel-microcode"
WICVARS:append = " ALTERNATIVE_KERNELS IMAGE_INSTALL PREFERRED_PROVIDER_virtual/kernel"
WIC_ROOTFS_EXTRA_SPACE ?= "10M"
