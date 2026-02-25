// Funkcja obsługująca ładowanie danych do formularza edycji
function loadRecordForEdit(btn) {
    const weight = btn.getAttribute('data-weight');
    const date = btn.getAttribute('data-date');

    document.getElementById('new-weight').value = weight;
    document.getElementById('new-date').value = date;

    document.getElementById('weight-form').scrollIntoView({ behavior: 'smooth' });
}

// Inicjalizacja wykresu
let weightChart = null;

function initChart() {
    const chartElement = document.getElementById('weight-chart');
    if (!chartElement) return;

    const ctx = chartElement.getContext('2d');

    // Dane pobieramy z globalnych zmiennych zdefiniowanych w HTML
    const data = {
        labels: typeof chartLabels !== 'undefined' ? chartLabels : [],
        datasets: [{
            label: typeof chartWeightLabel !== 'undefined' ? chartWeightLabel : 'Waga (kg)',
            data: typeof chartData !== 'undefined' ? chartData : [],
            borderColor: '#0984e3',
            backgroundColor: 'rgba(9, 132, 227, 0.1)',
            tension: 0.3,
            fill: true
        }]
    };

    weightChart = new Chart(ctx, {
        type: 'line',
        data: data,
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: { beginAtZero: false }
            },
            plugins: {
                legend: { display: false }
            },
            onClick: (e) => {
                if (!weightChart) return;

                const points = weightChart.getElementsAtEventForMode(e, 'nearest', { intersect: true }, true);
                if (points.length) {
                    const firstPoint = points[0];
                    const index = firstPoint.index;

                    if (typeof chartRecordIds !== 'undefined' && chartRecordIds.length > index) {
                        const recordId = chartRecordIds[index];
                        const date = data.labels[index];
                        const weight = data.datasets[0].data[index];

                        // Populate modal inputs
                        const editWeightInput = document.getElementById('edit-weight');
                        const editDateDisplay = document.getElementById('edit-date-display');
                        const editDateHidden = document.getElementById('edit-date-hidden');
                        const editForm = document.getElementById('edit-weight-form');
                        const deleteForm = document.getElementById('delete-weight-form');

                        if (editWeightInput && deleteForm && editForm) {
                            editWeightInput.value = weight;
                            if (editDateDisplay) editDateDisplay.innerText = date;
                            if (editDateHidden) editDateHidden.value = date;

                            // Ensure the forms submit to the correct URLs
                            editForm.action = '/profile/edit-weight/' + recordId;
                            deleteForm.action = '/profile/delete-weight/' + recordId;

                            // Open modal
                            if (typeof openEditWeightModal === 'function') {
                                openEditWeightModal();
                            }
                        }
                    }
                }
            }
        }
    });
}

// Uruchomienie po załadowaniu DOM
document.addEventListener('DOMContentLoaded', function () {
    initChart();
});