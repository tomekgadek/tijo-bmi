document.addEventListener('DOMContentLoaded', function () {
    const chartElement = document.getElementById('adminBmiChart');

    if (!chartElement) return;

    // Pobieranie danych z atrybutów HTML
    const usernames = JSON.parse(chartElement.dataset.usernames || '[]');
    const currentBMIs = JSON.parse(chartElement.dataset.bmis || '[]');

    const ctx = chartElement.getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: usernames,
            datasets: [{
                label: 'BMI',
                data: currentBMIs,
                backgroundColor: 'rgba(9, 132, 227, 0.5)',
                borderColor: '#0984e3',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: { beginAtZero: true }
            },
            plugins: {
                legend: { display: false }
            }
        }
    });
});