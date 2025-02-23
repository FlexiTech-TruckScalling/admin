<%@ include file="../../includes/import-tags.jsp"%>

<form:form action="counter-setting.fxt" modelAttribute="counterDTO"
	method="post" id="common-form">
	<div class="card shadow mb-4">
		<div class="card-header py-3">
			<h6 class="m-0 font-weight-bold text-primary">Counter Setting</h6>
		</div>
		<div class="card-body">

			<div class="row">
				<div class="col-md-6">
					<form:label path="id" class="form-label">Counter <strong
							class="text-danger">*</strong>
					</form:label>
					<form:select path="id"
						class="form-control border bg-white selectpicker required counter-setting-counter">
						<option value="-1" disabled selected>-- Select One --</option>
						<form:options items="${counterList }" itemValue="id"
							itemLabel="name" />
					</form:select>
					<span class="text-danger input-error-msg" data-label="Counter"></span>
				</div>
			</div>

		</div>

	</div>

	<div class="card shadow mb-4">
		<div class="card-body">
			<ul class="nav nav-tabs" id="counterSettingsTabs" role="tablist">
				<c:forEach items="${counterDTO.settingCategoryDTOs}" var="category"
					varStatus="cLoop">
					<li class="nav-item" role="presentation"><a
						class="nav-link ${cLoop.index == 0 ? 'active' : ''}"
						id="tab-${category.id}" data-toggle="tab"
						href="#content-${category.id}" role="tab"
						aria-controls="content-${category.id}"
						aria-selected="${cLoop.index == 0 ? 'true' : 'false'}">
							${category.description} </a></li>
				</c:forEach>
			</ul>

			<div class="tab-content" id="counterSettingsContent">
				<c:forEach items="${counterDTO.settingCategoryDTOs}" var="category"
					varStatus="cLoop">
					<div class="tab-pane fade ${cLoop.index == 0 ? 'show active' : ''}"
						id="content-${category.id}" role="tabpanel"
						aria-labelledby="tab-${category.id}">
						<div class="card-body">
							<div class="row px-2 mt-3">
								<fieldset class="col-md-12 rounded">
									<legend>${category.description}</legend>
									<div class="row">
										<c:forEach items="${category.counterSettingDTOs}"
											var="setting" varStatus="loop">
											<div class="col-md-3 col-sm-12 mb-2">
												<form:label
													path="settingCategoryDTOs[${cLoop.index}].counterSettingDTOs[${loop.index }].value"
													class="form-label text-truncate" style="max-width: 200px;">${setting.masterCounterSettingDTO.description} 
                                        <strong class="text-danger">*</strong>
												</form:label>
												<c:choose>
													<c:when
														test="${setting.masterCounterSettingDTO.code == 'INOUT_BOUND'}">
														<form:select
															path="settingCategoryDTOs[${cLoop.index}].counterSettingDTOs[${loop.index }].value"
															class="form-control border bg-white selectpicker required">
															<option value="-1" disabled selected>-- None --</option>
															<form:options items="${inOutBoundList}" itemValue="code"
																itemLabel="desc" />
														</form:select>
													</c:when>
													<c:when
														test="${setting.masterCounterSettingDTO.code == 'DEFAULT_WEIGHT_UNIT'}">
														<form:select
															path="settingCategoryDTOs[${cLoop.index}].counterSettingDTOs[${loop.index }].value"
															class="form-control border bg-white selectpicker required">
															<option value="-1" disabled selected>-- None --</option>
															<form:options items="${weigthUnitList}" itemValue="code"
																itemLabel="name" />
														</form:select>
													</c:when>
													<c:otherwise>
														<form:input
															path="settingCategoryDTOs[${cLoop.index}].counterSettingDTOs[${loop.index }].value"
															type="text" class="form-control required"
															placeholder="${setting.masterCounterSettingDTO.description}" />
													</c:otherwise>
												</c:choose>
												<span class="text-danger input-error-msg"
													data-label="${setting.masterCounterSettingDTO.description}"></span>
												<form:hidden
													path="settingCategoryDTOs[${cLoop.index}].counterSettingDTOs[${loop.index }].id" />
												<form:hidden
													path="settingCategoryDTOs[${cLoop.index}].counterSettingDTOs[${loop.index }].masterCounterSettingDTO.id"
													value="${setting.masterCounterSettingDTO.id}" />
											</div>
										</c:forEach>
									</div>
								</fieldset>
							</div>
							<div class="row mt-4">
								<div class="col text-left">
									<button type="submit" class="btn btn-primary">Save</button>
									<a href="counter-setting.fxt" class="btn btn-secondary">Clear</a>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>

	<%-- <c:forEach items="${counterDTO.settingCategoryDTOs }" var="category"
		varStatus="cLoop">
		<div class="card shadow mb-4">
			<a href="#collapseCard${category.id }"
				class="d-block card-header py-3" data-toggle="collapse"
				role="button" aria-expanded="true"
				aria-controls="collapseCardExample">
				<h6 class="m-0 font-weight-bold text-primary">${category.description}</h6>
			</a>
			<div class="collapse show"
				id="collapseCard${category.id }">
				<div class="card-body">
					<div class="row px-2 mt-3">
						<fieldset class="col-md-12 rounded">
							<legend>${category.description }</legend>
							<div class="row">
								<c:forEach items="${category.counterSettingDTOs }" var="setting"
									varStatus="loop">
									<div class="col-md-3 col-sm-12 mb-2">
										<form:label
											path="settingCategoryDTOs[${cLoop.index}].counterSettingDTOs[${loop.index }].value"
											class="form-label">${setting.masterCounterSettingDTO.description } <strong
												class="text-danger">*</strong>
										</form:label>
										<c:choose>
											<c:when
												test="${setting.masterCounterSettingDTO.code == 'INOUT_BOUND'  }">
												<form:select
													path="settingCategoryDTOs[${cLoop.index}].counterSettingDTOs[${loop.index }].value"
													class="form-control border bg-white selectpicker required">
													<option value="-1" disabled selected>-- None --</option>
													<form:options items="${inOutBoundList }" itemValue="code"
														itemLabel="desc" />
												</form:select>
											</c:when>
											<c:when
												test="${setting.masterCounterSettingDTO.code == 'DEFAULT_WEIGHT_UNIT'  }">
												<form:select
													path="settingCategoryDTOs[${cLoop.index}].counterSettingDTOs[${loop.index }].value"
													class="form-control border bg-white selectpicker required">
													<option value="-1" disabled selected>-- None --</option>
													<form:options items="${weigthUnitList }" itemValue="code"
														itemLabel="name" />
												</form:select>
											</c:when>
											<c:otherwise>
												<form:input
													path="settingCategoryDTOs[${cLoop.index}].counterSettingDTOs[${loop.index }].value"
													type="text" class="form-control required"
													placeholder="${setting.masterCounterSettingDTO.description }" />
											</c:otherwise>
										</c:choose>

										<span class="text-danger input-error-msg"
											data-label="${setting.masterCounterSettingDTO.description }"></span>
										<form:hidden
											path="settingCategoryDTOs[${cLoop.index}].counterSettingDTOs[${loop.index }].id" />
										<form:hidden
											path="settingCategoryDTOs[${cLoop.index}].counterSettingDTOs[${loop.index }].masterCounterSettingDTO.id"
											value="${setting.masterCounterSettingDTO.id}" />
									</div>
								</c:forEach>
							</div>
						</fieldset>
					</div>
					<div class="row mt-4">
						<div class="col text-left">
							<button type="submit" class="btn btn-primary">Save</button>
							<a href="counter-setting.fxt" class="btn btn-secondary">Clear</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</c:forEach> --%>
</form:form>
<script
	src="<%=request.getContextPath()%>/resources/js/counter-setting.js"></script>