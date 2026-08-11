$(document).ready(function() {
    let hourlyChart = null;
    let currentPreset = 'today';
    let currentStart = null;
    let currentEnd = null;
	let topCustomersChart = null;

    const presetLabels = {
        today: 'Today',
        week: 'This Week',
        month: 'This Month',
        year: 'This Year',
        custom: 'Custom Range'
    };

    function formatDate(d) {
        return d.getFullYear() + '-' +
            String(d.getMonth() + 1).padStart(2, '0') + '-' +
            String(d.getDate()).padStart(2, '0');
    }

    function formatDisplayDate(d) {
        return d.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
    }

    function getPresetRange(preset) {
        const today = new Date();
        let start = new Date(today);
        let end = new Date(today);

        switch (preset) {
            case 'today':
                break;
            case 'week':
                start.setDate(today.getDate() - today.getDay());
                break;
            case 'month':
                start = new Date(today.getFullYear(), today.getMonth(), 1);
                break;
            case 'year':
                start = new Date(today.getFullYear(), 0, 1);
                break;
        }
        return { start, end };
    }

    function setActiveRangeLabel(start, end) {
        $('#active-range-label').text(formatDisplayDate(start) + ' \u2013 ' + formatDisplayDate(end));
    }

    function showSkeletons() {
        $('.skeleton-container').show();
        $('.dash-content').hide();
        $('#recent-transactions-table').empty();
        $('#top-customers-list').empty();
        if (hourlyChart) hourlyChart.destroy();
    }

    function hideSkeletons() {
        $('.skeleton-container').hide();
        $('.dash-content').show();
    }

    function formatDateTime(dateTimeString) {
        if (!dateTimeString) return '';
        const date = new Date(dateTimeString);
        return date.getFullYear() + '-' +
            String(date.getMonth() + 1).padStart(2, '0') + '-' +
            String(date.getDate()).padStart(2, '0') + ' ' +
            String(date.getHours()).padStart(2, '0') + ':' +
            String(date.getMinutes()).padStart(2, '0');
    }

	function getStatusBadgeClass(status) {
	    switch (status) {
	        case 1: return 'bg-success';
	        case 2: return ' bg-warning text-dark';
	        case 3: return 'bg-danger';
	        default: return 'bg-secondary';
	    }
	}

	

	function loadDashboardData(start, end) {
	    if (typeof showSkeletons === 'function') showSkeletons();

	    const params = {};
	    if (start && end) {
	        params.fromDate = formatDate(start);
	        params.toDate = formatDate(end);
	        if (typeof setActiveRangeLabel === 'function') setActiveRangeLabel(start, end);
	    }

	    $.ajax({
	        url: CONTEXT_PATH + 'api/dashboard/summary.json',
	        type: 'GET',
	        data: params,
	        dataType: 'json',
	        success: function(data) {
	            $('#transactions-card .h5').text(data.todayTransactionCount != null ? data.todayTransactionCount : 0);
	            $('#weight-card .h5').text(data.todayTotalWeight != null ? data.todayTotalWeight : '0');
	            $('#revenue-card .h5').text(data.todayTotalRevenue != null ? (data.todayTotalRevenue) : '0');
	            $('#yard-card .h5').text(data.vehiclesInYard != null ? data.vehiclesInYard : 0);
	            $('#shifts-card .h5').text(data.activeShiftsCount != null ? data.activeShiftsCount : 0);

	            const $tableBody = $('#recent-transactions-table');
	            if ($tableBody.length) {
	                $tableBody.empty();
	                if (data.recentTransactions && data.recentTransactions.length > 0) {
	                    $.each(data.recentTransactions, function(index, tx) {
	                        const badgeClass = typeof getStatusBadgeClass === 'function' ? getStatusBadgeClass(tx.status) : 'bg-secondary';
	                        const statusLabel = tx.statusDesc || 'Unknown';
	                        const weight = tx.weight != null ? tx.weight : '0';
							const cargoWeight = tx.cargoWeight != null ? tx.cargoWeight : '0';

	                        const row = `<tr>
	                                        <td>${tx.ticketNo || '-'}</td>
	                                        <td>${tx.vehicleNumber || '-'}</td>
	                                        <td>${tx.customerName || '-'}</td>
	                                        <td>${tx.transactionDate || '-'}</td>
	                                        <td>${weight}</td>
											<td>${cargoWeight}</td>
	                                        <td><span class="badge ${badgeClass}">${statusLabel}</span></td>
	                                    </tr>`;
	                        $tableBody.append(row);
	                    });
	                } else {
	                    $tableBody.append('<tr><td colspan="7" class="text-center py-4 text-muted">No transactions found</td></tr>');
	                }
	            }

	            if (typeof hideSkeletons === 'function') hideSkeletons();

	            const topCanvas = document.getElementById('topCustomersChart');
	            if (topCanvas) {
	                if (data.topCustomers && data.topCustomers.length > 0) {
	                    $('#top-customers-chart-wrap').show();
	                    $('#top-customers-empty').hide();
	                    topCanvas.style.display = 'block';

	                    const labels = data.topCustomers.map(c => c.customerName || 'Unknown');
	                    const counts = data.topCustomers.map(c => c.transactionCount || 0);
	                    const palette = ['#4e73df', '#1cc88a', '#36b9cc', '#f6c23e', '#e74a3b', '#858796'];

	                    if (topCustomersChart) {
	                        topCustomersChart.destroy();
	                    }

	                    topCustomersChart = new Chart(topCanvas, {
	                        type: 'pie',
	                        data: {
	                            labels: labels,
	                            datasets: [{
	                                data: counts,
	                                backgroundColor: palette.slice(0, labels.length),
	                                borderWidth: 1
	                            }]
	                        },
	                        options: {
	                            responsive: true,
	                            maintainAspectRatio: false,
	                            plugins: {
	                                legend: { position: 'bottom', labels: { boxWidth: 12, padding: 12 } },
	                                tooltip: { callbacks: { label: (ctx) => `${ctx.label}: ${ctx.parsed} transaction(s)` } }
	                            }
	                        }
	                    });
	                } else {
	                    $('#top-customers-chart-wrap').show();
	                    $('#top-customers-empty').show();
	                    topCanvas.style.display = 'none';
	                }
	            }

	            const hourlyCanvas = document.getElementById('hourlyTransactionChart');
	            if (hourlyCanvas) {
	                const hourLabels = [];
	                const hourData = [];
	                for (let h = 0; h < 24; h++) {
	                    hourLabels.push((h % 12 === 0 ? 12 : h % 12) + (h < 12 ? ' AM' : ' PM'));
	                    const count = data.hourlyTransactionCounts ? data.hourlyTransactionCounts[h] : 0;
	                    hourData.push(count || 0);
	                }

	                if (hourlyChart) {
	                    hourlyChart.destroy();
	                }

	                hourlyChart = new Chart(hourlyCanvas, {
	                    type: 'bar',
	                    data: {
	                        labels: hourLabels,
	                        datasets: [{
	                            label: 'Transactions',
	                            data: hourData,
	                            backgroundColor: 'rgba(13, 110, 253, 0.6)',
	                            borderRadius: 4
	                        }]
	                    },
	                    options: {
	                        responsive: true,
	                        maintainAspectRatio: false,
	                        plugins: { legend: { display: false } },
	                        scales: { y: { beginAtZero: true, ticks: { precision: 0 } } }
	                    }
	                });
	            }
	        },
	        error: function(xhr, status, error) {
	            if (typeof hideSkeletons === 'function') hideSkeletons();
	            
	            let message = 'Failed to load dashboard data. Please try again.';
	            if (xhr.responseJSON && xhr.responseJSON.error) {
	                message = xhr.responseJSON.error;
	            }

	            console.error('Failed to load dashboard summary', xhr.status, message);
	            if (typeof showToast === 'function') showToast('error', message);
	            if (typeof showErrorState === 'function') showErrorState(message);
	        }
	    });
	}
	
	function showErrorState(message) {
	    $('#recent-transactions-table').html(
	        `<tr><td colspan="5" class="text-center py-4 text-danger">${message}</td></tr>`
	    );
	    $('#top-customers-list').html(
	        `<li class="list-group-item text-center py-4 text-danger">${message}</li>`
	    );
	    $('.dash-content').show();
	}
    function applyPreset(preset) {
        currentPreset = preset;
        $('.date-filter-option').removeClass('active');
        $(`.date-filter-option[data-preset="${preset}"]`).addClass('active');
        $('#date-filter-current').text(presetLabels[preset]);
        $('#custom-range-panel').removeClass('show');
        $('#date-filter').removeClass('open');

        const { start, end } = getPresetRange(preset);
        currentStart = start;
        currentEnd = end;
        loadDashboardData(start, end);
    }

    $('#date-filter-toggle').on('click', function(e) {
        e.stopPropagation();
        $('#date-filter').toggleClass('open');
    });

    $(document).on('click', function(e) {
        if (!$(e.target).closest('#date-filter').length) {
            $('#date-filter').removeClass('open');
            $('#custom-range-panel').removeClass('show');
        }
    });

    $(document).on('click', '.date-filter-option', function(e) {
        const preset = $(this).data('preset');

        if (preset === 'custom') {
            e.stopPropagation();
            $('.date-filter-option').removeClass('active');
            $(this).addClass('active');
            $('#custom-range-panel').addClass('show');

            if (currentStart && currentEnd) {
                $('#custom-start-date').val(formatDate(currentStart));
                $('#custom-end-date').val(formatDate(currentEnd));
            }
            return;
        }

        applyPreset(preset);
    });

    $('#apply-custom-range').on('click', function(e) {
        e.stopPropagation();
        const startVal = $('#custom-start-date').val();
        const endVal = $('#custom-end-date').val();

        if (!startVal || !endVal) return;

        const start = new Date(startVal);
        const end = new Date(endVal);

        if (start > end) {
            alert('Start date must be before end date.');
            return;
        }

        currentPreset = 'custom';
        currentStart = start;
        currentEnd = end;
        $('#date-filter-current').text('Custom Range');
        $('#date-filter').removeClass('open');
        $('#custom-range-panel').removeClass('show');

        loadDashboardData(start, end);
    });

    $('#refresh-btn').on('click', function() {
        loadDashboardData(currentStart, currentEnd);
    });

    applyPreset('today');
});