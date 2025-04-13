<%@ include file="../../includes/import-tags.jsp"%>

<div class="">
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Company Management</h6>
        </div>
        <div class="card-body">

            <form:form action="company.fxt" modelAttribute="companyDTO" 
                method="post" id="common-form" enctype="multipart/form-data">
                <form:hidden path="id" />
                
                <div class="row g-3">
                    <!-- Name -->
                    <div class="col-md-4">
                        <form:label path="name" class="form-label">Name <strong class="text-danger">*</strong></form:label>
                        <form:input path="name" type="text" class="form-control required" 
                            placeholder="Company name" />
                        <span class="text-danger input-error-msg" data-label="Company Name"></span>
                    </div>

                    <!-- Contact Person -->
                    <div class="col-md-4">
                        <form:label path="contactPerson" class="form-label">Contact Person 
                            <strong class="text-danger">*</strong></form:label>
                        <form:input path="contactPerson" type="text" class="form-control required" 
                            placeholder="Contact Person" />
                        <span class="text-danger input-error-msg" data-label="Contact Person"></span>
                    </div>

                    <!-- Contact Phone -->
                    <div class="col-md-4">
                        <form:label path="contactPhone" class="form-label">Contact Phone 
                            <strong class="text-danger">*</strong></form:label>
                        <form:input path="contactPhone" type="text" class="form-control required" 
                            placeholder="Contact Phone" />
                        <span class="text-danger input-error-msg" data-label="Contact Phone"></span>
                    </div>
                </div>

                <div class="row g-3 mt-3">
                    <!-- Address -->
                    <div class="col-md-12">
                        <form:label path="address" class="form-label">Address 
                            <strong class="text-danger">*</strong></form:label>
                        <form:textarea path="address" type="text" class="form-control required" 
                            placeholder="Address" />
                        <span class="text-danger input-error-msg" data-label="Address"></span>
                    </div>
                </div>

                <div class="row g-3 mt-3">
                    

                    <!-- Logo Upload -->
                    <div class="col-md-4">
                        <form:label path="logoImage" class="form-label">Company Logo</form:label>
                        <input type="file" name="logoImage" id="logoImage" 
                            class="form-control" accept="image/*" />
                        <span class="text-danger input-error-msg" data-label="Company Logo"></span>
                        
                        <div class="mt-2">
                            <img id="logoPreview" src="/images/logo/${companyDTO.companyLogo}" 
                                alt="Logo Preview" class="img-thumbnail" 
                                style="max-width: 200px; max-height: 200px;" 
                                onerror="this.src='data:image/gif;base64,R0lGODlhAQABAIAAAMLCwgAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw=='" />
                        </div>
                    </div>

                    <!-- Status -->
                    <div class="col-md-4">
                        <form:label path="status" class="form-label">Status 
                            <strong class="text-danger">*</strong></form:label>
                        <form:select path="status" class="form-control border bg-white selectpicker required">
                            <option value="-1" disabled selected>-- Select One --</option>
                            <form:options items="${statusList}" itemValue="code" itemLabel="desc" />
                        </form:select>
                        <span class="text-danger input-error-msg" data-label="Status"></span>
                    </div>
                </div>

                <!-- Action Buttons -->
                <div class="row mt-4">
                    <div class="col text-left">
                        <button type="submit" class="btn btn-primary">
                            ${empty companyDTO.id ? "Save" : "Update"}
                        </button>
                        <a href="company.fxt" class="btn btn-secondary">Clear</a>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>

<script>
// Image preview handler
document.getElementById('logoImage').addEventListener('change', function(e) {
    const file = e.target.files[0];
    const preview = document.getElementById('logoPreview');
    
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            preview.src = e.target.result;
            preview.style.display = 'block';
        };
        reader.readAsDataURL(file);
    } else {
        preview.src = '#';
        preview.style.display = 'none';
    }
});
</script>