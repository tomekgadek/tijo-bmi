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
            label: 'Waga (kg)',
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
            }
        }
    });
}

// Uruchomienie po załadowaniu DOM
document.addEventListener('DOMContentLoaded', function() {
    initChart();
});