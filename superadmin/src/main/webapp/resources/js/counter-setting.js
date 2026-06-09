/**
 * 
 */
$(document).ready(function() {
	$('.counter-setting-counter').on('change', function() {
		const selectedId = this.value;
		if (selectedId) {
			const currentUrl = window.location.origin + window.location.pathname;
			const newUrl = `${currentUrl}?counterId=${selectedId}`;

			// Reload the page with the new URL
			window.location.href = newUrl;
		}
	})
})