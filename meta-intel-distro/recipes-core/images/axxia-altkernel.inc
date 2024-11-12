IMGCLASSES:remove = "image-live"
IMGCLASSES:append = "${@bb.utils.contains_any('IMAGE_FSTYPES', 'live iso hddimg', ' image-live-altkernel', '', d)}"

IMAGE_INSTALL:append = " ${@oe.utils.conditional('ALTERNATIVE_KERNELS', '', '', ' \
						${ALTERNATIVE_KERNELS_INSTALL} \
						${ALTERNATIVE_KERNELS_MODULES_INSTALL} \
						${ALTERNATIVE_KERNELS_LTTNG_MODULES}', d)} \
						"
