/**
 * 
 */
// Automatically show the toast if errorMsg is not empty
document.addEventListener('DOMContentLoaded', () => {
	const errorToastElement = document.getElementById('errorToast');
	let errorToast;
	if (errorToastElement) {
		errorToast = new bootstrap.Toast(errorToastElement, { autohide: false });
		errorToast.show();

		$('.toast .btn-close').on('click', function() {
			errorToast.hide();
		})

		setTimeout(() => {
			errorToast.hide();
		}, 5000);
	}

	const successToastElement = document.getElementById('successToast');
	let successToast;
	if (successToastElement) {
		successToast = new bootstrap.Toast(successToastElement, { autohide: false });
		successToast.show();

		$('.toast .btn-close').on('click', function() {
			successToast.hide();
		})
		setTimeout(() => {
			successToast.hide();
		}, 5000);
	}
});

$(()=>{
	$("button.delete-data").on('click', function(){
		var action = $(this).data('action');
		var id = $(this).data('id');
		
		if(action && id){
			$('#delete-modal-form').attr('action', action)
			$('#delete-id').val(id);
			$('#deleteModal').modal('show');
		}
	})
})