<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
body {
	background-color: var(--light-bg);
	color: var(--secondary-color);
}

.skeleton {
	background: linear-gradient(90deg, #f1f5f9 25%, var(--border-color) 50%,
		#f1f5f9 75%);
	background-size: 200% 100%;
	animation: shimmer 1.5s infinite;
	border-radius: 4px;
	display: inline-block;
}

.skeleton-text {
	height: 20px;
	width: 100%;
	margin-bottom: 0.25rem;
}

.skeleton-chart {
	height: 240px;
	width: 100%;
	border-radius: 8px;
}

.skeleton-table-row {
	height: 32px;
	width: 100%;
	margin-bottom: 0.4rem;
	border-radius: 4px;
}

@keyframes shimmer { 
    0% { background-position: -200% 0; }
    100% { background-position: 200% 0; }
}

.dash-content {
	display: none;
}

.card {
	border: 1px solid var(--border-color) !important;
	border-radius: 12px !important;
	box-shadow: 0 2px 4px -1px var(--shadow-color) !important;
	transition: all 0.2s ease;
}

.card:hover {
	box-shadow: 0 6px 12px -2px var(--shadow-color) !important;
}

.card-header {
	background-color: transparent !important;
	border-bottom: 1px solid var(--border-color) !important;
	padding: 0.85rem 1.2rem !important;
	font-weight: 600;
}

.card-body {
	padding: 1rem 1.2rem !important;
}

.stat-card {
	position: relative;
	overflow: hidden;
}

.stat-icon {
	width: 38px;
	height: 38px;
	display: flex;
	align-items: center;
	justify-content: center;
	border-radius: 8px;
	font-size: 0.9rem;
}

#transactions-card .stat-icon {
	background-color: rgba(43, 108, 176, 0.1);
	color: var(--primary-color);
}

#weight-card .stat-icon {
	background-color: rgba(2, 132, 199, 0.1);
	color: var(--info-color);
}

#revenue-card .stat-icon {
	background-color: rgba(22, 163, 74, 0.1);
	color: var(--success-color);
}

#yard-card .stat-icon {
	background-color: rgba(202, 138, 4, 0.1);
	color: #ca8a04;
}

#shifts-card .stat-icon {
	background-color: rgba(99, 102, 241, 0.1);
	color: #6366f1;
}

.page-header h1 {
	font-weight: 700;
	letter-spacing: -0.025em;
	color: #0f172a;
	font-size: 1.5rem;
}

.btn {
	border-radius: 8px;
	font-weight: 500;
	padding: 0.35rem 0.75rem;
	font-size: 0.875rem;
}

.btn-outline-primary {
	border-color: var(--border-color);
	color: #0f172a;
	background: white;
}

.btn-outline-primary:hover {
	background-color: rgba(43, 108, 176, 0.1);
	border-color: var(--primary-color);
	color: var(--primary-color);
}

.table {
	margin-bottom: 0;
}

.table th {
	font-weight: 600;
	font-size: 0.7rem;
	text-transform: uppercase;
	letter-spacing: 0.05em;
	color: var(--secondary-color);
	background-color: var(--light-bg) !important;
	border-bottom: 1px solid var(--border-color);
	padding: 0.65rem 1rem;
}

.table td {
	padding: 0.65rem 1rem;
	border-bottom: 1px solid var(--border-color);
	vertical-align: middle;
	font-size: 0.875rem;
}

.table-hover tbody tr:hover {
	background-color: var(--light-bg);
}

.list-group-item {
	border-color: var(--border-color) !important;
	padding: 0.65rem 1rem;
	font-size: 0.875rem;
}

.dash-toolbar {
	display: flex;
	align-items: center;
	gap: 0.5rem;
	flex-wrap: wrap;
}

.date-filter {
	position: relative;
}

.date-filter-toggle {
	display: flex;
	align-items: center;
	gap: 8px;
	border: 1px solid var(--border-color);
	border-radius: 8px;
	padding: 0.4rem 0.85rem;
	font-size: 0.875rem;
	font-weight: 500;
	color: #0f172a;
	cursor: pointer;
}

.date-filter-toggle:hover {
	border-color: var(--primary-color);
	color: var(--primary-color);
}

.date-filter-toggle i.fa-chevron-down {
	font-size: 0.68rem;
	color: var(--secondary-color);
	transition: transform 0.2s ease;
}

.date-filter.open .date-filter-toggle i.fa-chevron-down {
	transform: rotate(180deg);
}

.date-filter-menu {
	position: absolute;
	top: calc(100% + 8px);
	right: 0;
	z-index: 1050;
	border: 1px solid var(--border-color);
	border-radius: 12px;
	box-shadow: 0 12px 28px -6px var(--shadow-color);
	min-width: 220px;
	padding: 0.4rem;
	display: none;
	background: #fff;
}

.date-filter.open .date-filter-menu {
	display: block;
}

.date-filter-option {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 0.5rem 0.7rem;
	border-radius: 8px;
	font-size: 0.85rem;
	cursor: pointer;
}

.date-filter-option:hover {
	background-color: var(--light-bg);
}

.date-filter-option.active {
	background-color: rgba(43, 108, 176, 0.1);
	color: var(--primary-color);
	font-weight: 600;
}

.date-filter-option.active i.fa-check {
	display: inline-block;
}

.date-filter-option i.fa-check {
	display: none;
	font-size: 0.72rem;
}

.date-filter-divider {
	height: 1px;
	background-color: var(--border-color);
	margin: 0.3rem 0.2rem;
}

.custom-range-panel {
	padding: 0.6rem 0.7rem 0.2rem;
	display: none;
}

.custom-range-panel.show {
	display: block;
}

.custom-range-panel label {
	font-size: 0.72rem;
	font-weight: 600;
	color: var(--secondary-color);
	margin-bottom: 0.2rem;
}

.custom-range-panel .form-control {
	font-size: 0.82rem;
	padding: 0.35rem 0.6rem;
	border-radius: 6px;
}

.custom-range-panel .btn-apply-range {
	width: 100%;
	margin-top: 0.5rem;
	background-color: var(--primary-color);
	color: #fff;
	border: none;
}

.active-range-badge {
	font-size: 0.72rem;
	font-weight: 500;
	color: var(--secondary-color);
}

@media (max-width: 768px) {
	.date-filter-menu {
		right: auto;
		left: 0;
	}
}
</style>

<div class="page-header mb-3 d-flex justify-content-between align-items-center flex-wrap gap-2">
	<div>
		<h1 class="mb-0">Truck Scale Dashboard</h1>
		<p class="text-muted mb-0 small">Weighbridge operations overview</p>
	</div>

	<div class="dash-toolbar">
		<span class="active-range-badge d-none d-md-inline" id="active-range-label"></span>

		<div class="date-filter" id="date-filter">
			<button type="button" class="date-filter-toggle" id="date-filter-toggle">
				<i class="far fa-calendar-alt"></i> <span id="date-filter-current">Today</span>
				<i class="fas fa-chevron-down"></i>
			</button>

			<div class="date-filter-menu" id="date-filter-menu">
				<div class="date-filter-option active" data-preset="today">
					<span>Today</span><i class="fas fa-check"></i>
				</div>
				<div class="date-filter-option" data-preset="week">
					<span>This Week</span><i class="fas fa-check"></i>
				</div>
				<div class="date-filter-option" data-preset="month">
					<span>This Month</span><i class="fas fa-check"></i>
				</div>
				<div class="date-filter-option" data-preset="year">
					<span>This Year</span><i class="fas fa-check"></i>
				</div>

				<div class="date-filter-divider"></div>

				<div class="date-filter-option" data-preset="custom">
					<span>Custom Range</span><i class="fas fa-check"></i>
				</div>

				<div class="custom-range-panel" id="custom-range-panel">
					<label>Start Date</label> <input type="date" class="form-control mb-2" id="custom-start-date"> 
					<label>End Date</label> <input type="date" class="form-control" id="custom-end-date">
					<button type="button" class="btn btn-sm btn-apply-range" id="apply-custom-range">Apply</button>
				</div>
			</div>
		</div>

		<button id="refresh-btn" class="btn btn-outline-primary btn-sm d-flex align-items-center gap-2">
			<i class="fas fa-sync-alt"></i> <span class="d-none d-sm-inline">Refresh</span>
		</button>
	</div>
</div>

<div class="row mb-3 g-3" id="stat-cards-container">
	<div class="col-xl-4 col-md-6 mb-3" id="transactions-card">
		<div class="card stat-card h-100">
			<div class="card-body">
				<div class="d-flex align-items-center justify-content-between">
					<div>
						<div class="text-muted text-uppercase mb-1" style="font-size: 0.7rem;">Transactions</div>
						<div class="skeleton-container">
							<div class="skeleton skeleton-text" style="width: 70px;"></div>
						</div>
						<div class="h5 mb-0 font-weight-bold text-dark dash-content"></div>
					</div>
					<div class="stat-icon">
						<i class="fas fa-truck-loading"></i>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="col-xl-4 col-md-6 mb-3" id="weight-card">
		<div class="card stat-card h-100">
			<div class="card-body">
				<div class="d-flex align-items-center justify-content-between">
					<div>
						<div class="text-muted text-uppercase mb-1" style="font-size: 0.7rem;">Total Weight (kg)</div>
						<div class="skeleton-container">
							<div class="skeleton skeleton-text" style="width: 90px;"></div>
						</div>
						<div class="h5 mb-0 font-weight-bold text-dark dash-content"></div>
					</div>
					<div class="stat-icon">
						<i class="fas fa-weight-hanging"></i>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="col-xl-4 col-md-6 mb-3" id="revenue-card">
		<div class="card stat-card h-100">
			<div class="card-body">
				<div class="d-flex align-items-center justify-content-between">
					<div>
						<div class="text-muted text-uppercase mb-1" style="font-size: 0.7rem;">Total Revenue</div>
						<div class="skeleton-container">
							<div class="skeleton skeleton-text" style="width: 90px;"></div>
						</div>
						<div class="h5 mb-0 font-weight-bold text-dark dash-content"></div>
					</div>
					<div class="stat-icon">
						<i class="fas fa-dollar-sign"></i>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="col-xl-6 col-md-6 mb-3" id="yard-card">
		<div class="card stat-card h-100">
			<div class="card-body">
				<div class="d-flex align-items-center justify-content-between">
					<div>
						<div class="text-muted text-uppercase mb-1" style="font-size: 0.7rem;">Vehicles In Yard</div>
						<div class="skeleton-container">
							<div class="skeleton skeleton-text" style="width: 60px;"></div>
						</div>
						<div class="h5 mb-0 font-weight-bold text-dark dash-content"></div>
					</div>
					<div class="stat-icon">
						<i class="fas fa-truck"></i>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="col-xl-6 col-md-6 mb-3" id="shifts-card">
		<div class="card stat-card h-100">
			<div class="card-body">
				<div class="d-flex align-items-center justify-content-between">
					<div>
						<div class="text-muted text-uppercase mb-1" style="font-size: 0.7rem;">Active Shifts</div>
						<div class="skeleton-container">
							<div class="skeleton skeleton-text" style="width: 50px;"></div>
						</div>
						<div class="h5 mb-0 font-weight-bold text-dark dash-content"></div>
					</div>
					<div class="stat-icon">
						<i class="fas fa-user-clock"></i>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="row mb-3 g-3">
	<div class="col-xl-8 mb-3 col-lg-7">
		<div class="card h-100">
			<div class="card-header">
				<h6 class="m-0 font-weight-bold text-dark small">Transactions by Hour</h6>
			</div>
			<div class="card-body">
				<div class="skeleton-container">
					<div class="skeleton skeleton-chart" style="height: 240px;"></div>
				</div>
				<div class="dash-content" style="position: relative; height: 240px; width: 100%">
					<canvas id="hourlyTransactionChart"></canvas>
				</div>
			</div>
		</div>
	</div>

	<div class="col-xl-4 mb-3 col-lg-5">
		<div class="card h-100">
			<div class="card-header">
				<h6 class="m-0 font-weight-bold text-dark small">Top Customers</h6>
			</div>
			<div class="card-body p-0">
				<div class="skeleton-container p-3">
					<div class="skeleton skeleton-table-row"></div>
					<div class="skeleton skeleton-table-row"></div>
				</div>
				<div class="dash-content p-3" id="top-customers-chart-wrap" style="display: none;">
					<canvas id="topCustomersChart" height="220"></canvas>
					<div id="top-customers-empty" class="text-center py-4 text-muted" style="display: none;">No customer data</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="row mb-3 g-3">
	<div class="col-12">
		<div class="card h-100">
			<div class="card-header">
				<h6 class="m-0 font-weight-bold text-dark small">Recent Transactions</h6>
			</div>
			<div class="card-body p-0">
				<div class="skeleton-container p-3">
					<div class="skeleton skeleton-table-row"></div>
					<div class="skeleton skeleton-table-row"></div>
					<div class="skeleton skeleton-table-row"></div>
				</div>
				<div class="table-responsive dash-content">
					<table class="table table-hover align-middle mb-0">
						<thead>
							<tr>
								<th>Code</th>
								<th>Vehicle No</th>
								<th>Customer</th>
								<th>Date/Time</th>
								<th>Weight (kg)</th>
								<th>Cargo Weight (kg)</th>
								<th>Status</th>
							</tr>
						</thead>
						<tbody id="recent-transactions-table"></tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>

<script>
	var CONTEXT_PATH = "${pageContext.request.contextPath}/";
</script>
<script src="${pageContext.request.contextPath}/resources/plugins/chart/chart.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/dashboard/dashboard.js"></script>