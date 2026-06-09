<%@ include file="../../includes/import-tags.jsp"%>

<ul
	class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion"
	id="accordionSidebar">
	<!-- Sidebar - Brand -->
	<a
		class="sidebar-brand d-flex align-items-center justify-content-center"
		href="">
		<div class="sidebar-brand-icon">
			<img
				src="<%=request.getContextPath()%>/resources/imgs/logo/logo-03.png"
				class="header-logo" />
		</div>
		<div class="sidebar-brand-text mx-3">Truck Scale</div>
	</a>

	<!-- Divider -->
	<hr class="sidebar-divider my-0">

	<!-- Nav Item - Dashboard -->
	<li class="nav-item active"><a class="nav-link"
		href="dashboard.fxt"> <i class="fas fa-fw fa-tachometer-alt"></i>
			<span>Dashboard</span>
	</a></li>

	<!-- Divider -->
	<hr class="sidebar-divider">

	<!-- Heading -->
	<div class="sidebar-heading">Interface</div>

	<!-- Parent Menu Loop -->
	<c:forEach var="parentMenu" items="${sessionScope.USER_MENUS}">
		<li class="nav-item"><a class="nav-link collapsed" href="#"
			data-toggle="collapse" data-target="#collapse${parentMenu.key.id}"
			aria-expanded="true" aria-controls="collapse${parentMenu.key.id}">
				<i class="fas fa-fw ${parentMenu.key.icon }"></i> <span>${parentMenu.key.name}</span>
		</a>
			<div id="collapse${parentMenu.key.id}" class="collapse"
				aria-labelledby="heading${parentMenu.key.id}"
				data-parent="#accordionSidebar">
				<div class="bg-white py-2 collapse-inner rounded">
					<h6 class="collapse-header">${parentMenu.key.description }</h6>

					<!-- Child Menu Loop -->
					<c:forEach var="childMenu" items="${parentMenu.value}">
						
						<a class="collapse-item" href="<%=request.getContextPath()%>/${childMenu.url}">${childMenu.name}</a>
					</c:forEach>
				</div>
			</div></li>
	</c:forEach>

	<!-- Divider -->
	<hr class="sidebar-divider d-none d-md-block">

	<!-- Sidebar Toggler (Sidebar) -->
	<div class="text-center d-none d-md-inline">
		<button class="rounded-circle border-0" id="sidebarToggle"></button>
	</div>
</ul>
