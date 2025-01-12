<%@ include file="../../includes/import-tags.jsp"%>

<div class="">
	<div class="card shadow mb-4">
		<div class="card-header py-3">
			<h6 class="m-0 font-weight-bold text-primary">Products Manage</h6>
		</div>
		<div class="card-body">

			<form:form action="products-manage.fxt" modelAttribute="productDTO"
				method="post" id="common-form">
				<form:hidden path="id" />
				<div class="row g-3">
					<!-- Name -->
					<div class="col-md-4">
						<form:label path="name" class="form-label">Name <strong
								class="text-danger">*</strong>
						</form:label>
						<form:input path="name" type="text" class="form-control required"
							placeholder="Product name" />
						<span class="text-danger input-error-msg" data-label="Name"></span>
					</div>

					<!-- Code -->
					<div class="col-md-4">
						<form:label path="code" class="form-label">Code<strong
								class="text-danger">*</strong>
						</form:label>
						<form:input path="code" type="number" min="0"
							class="form-control required" placeholder="Code" />
						<span class="text-danger input-error-msg" data-label="Code"></span>
					</div>

					<div class="col-md-4">
						<form:label path="status" class="form-label">Status <strong
								class="text-danger">*</strong>
						</form:label>
						<form:select path="status"
							class="form-control border bg-white selectpicker required">
							<option value="-1" disabled selected>-- Select One --</option>
							<form:options items="${statusList }" itemValue="code"
								itemLabel="desc" />
						</form:select>
						<span class="text-danger input-error-msg" data-label="Status"></span>
					</div>
					
					
				</div>
				<div class="row g-3 mt-3">
					<div class="col-md-4">
						<form:label path="goodsId" class="form-label">Goods <strong
								class="text-danger">*</strong>
						</form:label>
						<form:select path="goodsId"
							class="form-control border bg-white selectpicker">
							<option value="-1" disabled>-- Select One --</option>
							<form:options items="${goodList }" itemValue="id"
								itemLabel="name" />
						</form:select>
						<span class="text-danger input-error-msg" data-label="Status"></span>
					</div>
				</div>

				<!-- Submit Button -->
				<div class="row mt-4">
					<div class="col text-left">
						<button type="submit" class="btn btn-primary">${empty productDTO.id ? "Save":"Update" }</button>
						<a href="products-manage.fxt" class="btn btn-secondary">Clear</a>
						<c:if test="${not empty productDTO.id }">
								<button type="button" data-action="products-delete.fxt"
									data-id="${productDTO.id }" class="btn btn-danger delete-data">Delete</button>
							</c:if>
					</div>
				</div>
			</form:form>
		</div>
	</div>

	<div class="card mb-4">
		<div class="card-header py-3">
			<span class="m-0 font-weight-bold text-secondary">Total
				Products</span>
		</div>
		<div class="card-body">
			<table class="table table-striped">
				<thead class="bg-gradient-primary text-white">
					<tr>
						<th class="py-1" scope="col" style="width: 50px;">#</th>
						<th class="py-1" scope="col">Name</th>
						<th class="py-1" scope="col">Code</th>
						<th class="py-1" scope="col">Goods</th>
						<th class="py-1" scope="col">Status</th>
						<th class="py-1" scope="col">Created Time</th>
						<th class="py-1" scope="col" style="width: 140px;">Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${empty productsList }">
						<tr>
							<td colspan="6" class="text-center">No products.</td>
						</tr>
					</c:if>
					<c:if test="${not empty productsList }">
						<c:forEach items="${productsList }" var="counter" varStatus="loop">
							<tr
								class="${not empty new_id && new_id == counter.id || productDTO.id == counter.id ? 'font-weight-bold text-info':'' }">
								<td>${loop.index + 1 }</td>
								<td>${counter.name }</td>
								<td>${counter.code }</td>
								<td>${counter.goodNames }</td>
								<td><span
									class="badge ${counter.status == 1 ? 'badge-info':'badge-danger' }">
										${counter.statusDesc } </span></td>
								<td>${counter.createdTimeDesc }</td>
								<td>
									<a href="?id=${counter.id }"><i class="fas fa-fw fa-edit"></i>Edit</a>
								</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</div>
	</div>
</div>