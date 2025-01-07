<%@ include file="../../includes/import-tags.jsp"%>

<c:if test="${not empty errorMsg }">
	<div class="toast-container w-100 position-fixed top-0 p-3">
		<div id="errorToast" class="toast mx-auto" role="alert"
			aria-live="assertive" aria-atomic="true">
			<div
				class="toast-header bg-danger text-white justify-content-between">
				<strong class="me-auto">Error</strong>
				<button type="button" class="btn-close btn-close-white"
					data-bs-dismiss="toast" aria-label="Close"></button>
			</div>
			<div class="toast-body"  style="white-space: wrap;">${errorMsg}</div>
		</div>
	</div>
</c:if>
<c:if test="${not empty successMsg }">
	<div class="toast-container w-100 position-fixed top-0 p-3">
		<div id="successToast" class="toast mx-auto" role="alert"
			aria-live="assertive" aria-atomic="true">
			<div
				class="toast-header bg-success text-white justify-content-between">
				<strong class="me-auto">Success</strong>
				<button type="button" class="btn-close btn-close-white"
					data-bs-dismiss="toast" aria-label="Close"></button>
			</div>
			<div class="toast-body"  style="white-space: wrap;">${successMsg}</div>
		</div>
	</div>
</c:if>