



var ctx = document.getElementById("successRateChart").getContext("2d");
var ctxBuild = document.getElementById("buildDurationChart").getContext("2d");
var ctxArtifactsSize = document.getElementById("artifactsSize").getContext("2d");
var ctxTimeSpentQueue = document.getElementById("timeSpentQueue").getContext("2d");
var ctxTestCount = document.getElementById("testCount").getContext("2d");


function typeChartHandler(typeChart, labels, title, dictValues){

  let data = {};
  let allPerf = {};
  switch(typeChart) {
      case 'Bar':
        data = {
                  labels: labels,
                  datasets: [{
                    label: title,
                    data: dictValues,
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
       allPerf = {
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

      break;
      case 'Line':
      data = {
        labels: labels,
        datasets: [{
          label: title,
          data: dictValues,
          borderColor: [
            'rgba(0, 180, 33, 1)',
          ],
          tension: 0.1

        }]
      };

      allPerf = {
                              type: 'line',
                              data: data,

                            };


      break;

      case 'Scatter':

            data = {
              labels: labels,
              datasets: [{
                label: title,
                data: dictValues,
                borderColor: [
                  'rgba(0, 180, 33, 1)',
                ],
                tension: 0.1

              }]
            };

            allPerf = {
                                    type: 'radar',
                                    data: data,
                                    options:  {
                                                     scale: {
                                                         min: 0
                                                     },
                                                 },
                                  };


      break;

  }

return allPerf;
}


function formatLabelsDate(arrLabels, dateFormat, period) {
switch(period) {
    case 'DAY':
                 arrLabels.push(
                            dateFormat.getDate()+
                                       " "+(dateFormat.getHours())+":0"+(dateFormat.getMinutes())

                       );
    break;
    case 'WEEK':
    case 'MONTH':
                 arrLabels.push(
                            dateFormat.getDate()+
                                       "/"+(dateFormat.getMonth()+1)+
                                       "/"+dateFormat.getFullYear()
                       );
    break;

    case 'QUARTER':
    case 'YEAR':
                 arrLabels.push(
(dateFormat.getMonth()+1)+"/"+dateFormat.getFullYear()
                       );

    break;

}
console.log("arrLabels", arrLabels);
}



function sortOnKeys(dict, period) {

    var sorted = [];
    for(var key in dict) {
        console.log("key", key);
        sorted[sorted.length] = key;
        console.log("sorted", sorted);
    }
    sorted.sort();
    console.log("sorted2", sorted);
    var dataBuildDurationValues = [];
    var labelsB = [];
    for(var i = 0; i < sorted.length; i++) {
       dataBuildDurationValues.push(dict[sorted[i]]);
       console.log("dataBuildDurationValues", dataBuildDurationValues, dict[sorted[i]]);
       var dateFormat= new Date(parseInt(sorted[i]));
       console.log("dateFormat", dateFormat);
       //var period = document.querySelector(".period").textContent;
       console.log("period", period)
       formatLabelsDate(labelsB, dateFormat, period);
       console.log("labelsB", labelsB);
    }
    return [dataBuildDurationValues, labelsB];
}

// success rate chart settings

var successRateSelect = document.querySelector("#selectSuccess");

function createSuccessRateChart(){
var period = document.getElementById("selectSuccess").value;
var typeChart = document.getElementById("selectChartSR").value;
console.log("period", period);
console.log("typeChart", typeChart)
var successRate2 = document.querySelector("#successRateData").textContent;
  console.log(successRate2);
  var obj = JSON.parse(successRate2);
  console.log("json", obj);

  var dataSuccessRateValues = [];
  var dataSuccessRateDict = {};
console.log(obj.count);
   for (var key in obj){
  console.log("111", key);


dataSuccessRateDict[Date.parse(key)] = parseFloat(obj[key]);
  }
  console.log("dataSuccessRateDict: ", dataSuccessRateDict);

  dictSuccess = sortOnKeys(dataSuccessRateDict, period)[0];
  labelsSuccess = sortOnKeys(dataSuccessRateDict, period)[1];

 let allPerf = typeChartHandler(typeChart, labelsSuccess, 'Success rate', dictSuccess);



  if (perfChartJsCharts["successRateChart"]) perfChartJsCharts["successRateChart"].destroy();
  perfChartJsCharts["successRateChart"] = new Chart(ctx, allPerf);

}


// test count chart settings

var testCountSelect = document.querySelector("#selectTestCount");

function createTestCountChart(){
var period = document.getElementById("selectTestCount").value;
var typeChart = document.getElementById("selectChartTC").value;
console.log("period", period)
var testCount2 = document.querySelector("#testCountData").textContent;
  console.log(testCount2);
  var obj = JSON.parse(testCount2);
  console.log("json", obj);

  var dataTestCountValues = [];
  var dataTestCountDict = {};
console.log(obj.count);
   for (var key in obj){
  console.log("111", key);


dataTestCountDict[Date.parse(key)] = parseFloat(obj[key]);
  }
  console.log("dataTestCountDict: ", dataTestCountDict);

  dictTestCount = sortOnKeys(dataTestCountDict, period)[0];
  labelsTestCount = sortOnKeys(dataTestCountDict, period)[1];

let settingsTestCount = typeChartHandler(typeChart, labelsTestCount, 'Test Count', dictTestCount);

  if (perfChartJsCharts["testCountChart"]) perfChartJsCharts["testCountChart"].destroy();
  perfChartJsCharts["testCountChart"] = new Chart(ctxTestCount, settingsTestCount);

}


// build duration chart settings

var buildDurationSelect = document.querySelector("#selectBuildDuration");

function createBuildDurationChart(){
var period = document.getElementById("selectBuildDuration").value;
var typeChart = document.getElementById("selectChartBD").value;
console.log("period", period)
var buildDuration = document.querySelector("#buildDurationData").textContent;
  console.log(buildDuration);
  var obj = JSON.parse(buildDuration);
  console.log("json", obj);

  var dataBuildDurationValues = [];
  var dataBuildDurationDict = {};
console.log(obj.count);
   for (var key in obj){
  console.log("111", key);
  console.log("Date.parse(key)", Date.parse(key));


dataBuildDurationDict[Date.parse(key)] = parseFloat(obj[key]);
  }
  console.log("dataBuildDurationDict: ", dataBuildDurationDict);

  dictBuildDuration = sortOnKeys(dataBuildDurationDict, period)[0];
  labelsBuildDuration = sortOnKeys(dataBuildDurationDict, period)[1];


   let settingsBuildDuration = typeChartHandler(typeChart, labelsBuildDuration, 'Build duration', dictBuildDuration);
  if (perfChartJsCharts["buildDurationChart"]) perfChartJsCharts["buildDurationChart"].destroy();
  perfChartJsCharts["buildDurationChart"] = new Chart(ctxBuild, settingsBuildDuration);

}



// artifact size chart settings

var artifactSizeSelect = document.querySelector("#selectArtifactSize");

function createArtifactSizeChart(){
var period = document.getElementById("selectArtifactsSize").value;
var typeChart = document.getElementById("selectChartAS").value;
console.log("period", period)
var artifactSize = document.querySelector("#artifactSizeData").textContent;
  console.log(artifactSize);
  var obj = JSON.parse(artifactSize);
  console.log("json", obj);

  var dataArtifactSizeValues = [];
  var dataArtifactSizeDict = {};
console.log(obj.count);
   for (var key in obj){
  console.log("111", key);
  console.log("Date.parse(key)", Date.parse(key));


dataArtifactSizeDict[Date.parse(key)] = parseFloat(obj[key]);
  }
  console.log("dataArtifactSizeDict: ", dataArtifactSizeDict);

  dictArtifactSize = sortOnKeys(dataArtifactSizeDict, period)[0];
  labelsArtifactSize = sortOnKeys(dataArtifactSizeDict, period)[1];

let settingsArtifactSize = typeChartHandler(typeChart, labelsArtifactSize, 'Artifact Size', dictArtifactSize);
  if (perfChartJsCharts["artifactSizeChart"]) perfChartJsCharts["artifactSizeChart"].destroy();
  perfChartJsCharts["artifactSizeChart"] = new Chart(ctxArtifactsSize, settingsArtifactSize);

}

// time queue chart settings

var timeQueueSelect = document.querySelector("#selectTimeQueue");

function createTimeQueueChart(){
var period = document.getElementById("selectTimeQueue").value;
var typeChart = document.getElementById("selectChartTQ").value;
console.log("period", period)
var timeQueue = document.querySelector("#timeQueueData").textContent;
  console.log(timeQueue);
  var obj = JSON.parse(timeQueue);
  console.log("json", obj);

  var dataTimeQueueValues = [];
  var dataTimeQueueDict = {};
console.log(obj.count);
   for (var key in obj){
  console.log("111", key);
  console.log("Date.parse(key)", Date.parse(key));


dataTimeQueueDict[Date.parse(key)] = parseFloat(obj[key]);
  }
  console.log("dataTimeQueueDict: ", dataTimeQueueDict);

  dictTimeQueue = sortOnKeys(dataTimeQueueDict, period)[0];
  labelsTimeQueue = sortOnKeys(dataTimeQueueDict, period)[1];

   let settingsTimeQueue = typeChartHandler(typeChart, labelsTimeQueue, 'Time Spent In Queue', dictTimeQueue);
  if (perfChartJsCharts["timeQueueChart"]) perfChartJsCharts["timeQueueChart"].destroy();
  perfChartJsCharts["timeQueueChart"] = new Chart(ctxTimeSpentQueue, settingsTimeQueue);

}



