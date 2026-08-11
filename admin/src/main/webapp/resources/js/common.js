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

/**
 * type: 'success' | 'error'
 */
function showToast(type, message) {
    const isError = type === 'error';
    const bg = isError ? 'bg-danger' : 'bg-success';
    const icon = isError ? 'fa-circle-exclamation' : 'fa-circle-check';

    let container = document.querySelector('.toast-container');
    if (!container) {
        container = document.createElement('div');
        container.className = 'toast-container position-fixed bottom-0 end-0 p-3';
        document.body.appendChild(container);
    }

    const toastEl = document.createElement('div');
    toastEl.className = `toast align-items-center text-white ${bg} border-0`;
    toastEl.setAttribute('role', 'alert');
    toastEl.setAttribute('aria-live', 'assertive');
    toastEl.setAttribute('aria-atomic', 'true');
    toastEl.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">
                <i class="fas ${icon} me-2"></i>${message}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto toast-close-btn" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>`;

    container.appendChild(toastEl);

    const toast = new bootstrap.Toast(toastEl, { autohide: true, delay: 5000 });
    toast.show();

    toastEl.addEventListener('hidden.bs.toast', () => toastEl.remove());
}