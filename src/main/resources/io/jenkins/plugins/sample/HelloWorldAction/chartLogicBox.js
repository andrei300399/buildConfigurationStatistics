const labels = Array.from({length: 30}, (_, i) => i + 1);
const data = {
  labels: labels,
  datasets: [{
    label: 'Success rate',
    data: Array.from({length: 30}, () => Math.floor(Math.random() * 100)),
    backgroundColor: [
      'rgba(0, 255, 0, 0.5)',
    ],
    borderColor: [
      'rgb(0, 69, 36)',
    ],
    categoryPercentage: 1,
    borderWidth: 1,
    barPercentage: 1,
  }]
};

const dataBuildDuration = {
  labels: labels,
  datasets: [{
    label: 'Build duration',
    data: Array.from({length: 30}, () => Math.floor(Math.random() * 60)),
    borderColor: [
      'rgba(0, 180, 33, 1)',
    ],
    tension: 0.1

  }]
};

const dataTimeSpentQueue = {
  labels: labels,
  datasets: [{
    label: 'Time Spent In Queue',
    data: Array.from({length: 30}, () => Math.floor(Math.random() * 60)),
    borderColor: [
      'rgba(0, 180, 33, 1)',
    ],
    tension: 0.1

  }]
};
const dataTestCount = {
  labels: labels,
  datasets: [{
    label: 'Test Count',
    data: Array.from({length: 30}, () => Math.floor(Math.random() * 60)),
    borderColor: [
      'rgba(0, 180, 33, 1)',
    ],
    tension: 0.1

  }]
};
const dataArtifactsSize = {
  labels: labels,
  datasets: [{
    label: 'Artifacts Size',
    data: Array.from({length: 30}, () => Math.floor(Math.random() * 60)),
    borderColor: [
      'rgba(0, 180, 33, 1)',
    ],
    tension: 0.1

  }]
};








var allPerf = {
                        type: 'bar',
                        data: data,
                        options: {
                            scales: {
                                  y: {
                                    beginAtZero: true
                                  }
                                }
                        }
                      };


var settingsBuildDuration = {
                        type: 'line',
                        data: dataBuildDuration,
                      };
var settingsTimeSpentQueue = {
                        type: 'line',
                        data: dataTimeSpentQueue,
                      };
var settingsTestCount = {
                        type: 'line',
                        data: dataTestCount,
                      };
var settingsArtifactsSize = {
                        type: 'line',
                        data: dataArtifactsSize,
                      };
var ctx = document.getElementById("successRateChart").getContext("2d");
var ctxBuild = document.getElementById("buildDurationChart").getContext("2d");
var ctxTimeSpentQueue = document.getElementById("timeSpentQueue").getContext("2d");
var ctxTestCount = document.getElementById("testCount").getContext("2d");
var ctxArtifactsSize = document.getElementById("artifactsSize").getContext("2d");
perfChartJsCharts["successRateChart"] = new Chart(ctx, allPerf);
perfChartJsCharts["buildDurationChart"] = new Chart(ctxBuild, settingsBuildDuration);
perfChartJsCharts["timeSpentQueue"] = new Chart(ctxTimeSpentQueue, settingsTimeSpentQueue);
perfChartJsCharts["testCount"] = new Chart(ctxTestCount, settingsTestCount);
perfChartJsCharts["artifactsSize"] = new Chart(ctxArtifactsSize, settingsArtifactsSize);




