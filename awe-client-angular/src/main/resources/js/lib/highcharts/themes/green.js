/**
 * Blue theme for Highcharts JS
 * @author Pablo Vidal
 */

Highcharts.theme["green"] = {
  colors: [ "#DDDF0D", "#55BF3B", "#DF5353", "#7798BF", "#aaeeee", "#ff0066", "#eeaaee", "#55BF3B", "#DF5353", "#7798BF", "#aaeeee" ],
  chart: {
    backgroundColor: {
      linearGradient: {
        x1: 0,
        y1: 0,
        x2: 1,
        y2: 1
      },
      stops: [ [ 0, '#ffffff' ], [ 1, '#d3e7b0' ] ]
    },
    borderColor: '#C5C5E9',
    borderWidth: 2,
    className: 'dark-container',
    plotBackgroundColor: 'rgba(255, 255, 255, .1)',
    plotBorderColor: 'black',
    plotBorderWidth: 1,
    selectionMarkerFill: 'rgba(40, 40, 95, 0.5)'
  },
  title: {
    style: {
      color: '#0B0B01',
      font: 'bold 16px "Trebuchet MS", Verdana, sans-serif'
    }
  },
  subtitle: {
    style: {
      color: '#05050B',
      font: 'bold 12px "Trebuchet MS", Verdana, sans-serif'
    }
  },
  xAxis: {
    gridLineColor: '#7171A0',
    gridLineWidth: 1,
    labels: {
      style: {
        color: '#08080C'
      }
    },
    lineColor: '#060603',
    tickColor: '#060603',
    title: {
      style: {
        color: 'black',
        fontWeight: 'bold',
        fontSize: '12px',
        fontFamily: 'Trebuchet MS, Verdana, sans-serif'

      }
    }
  },
  yAxis: {
    gridLineColor: '#7171A0',
    labels: {
      style: {
        color: '#08080C'
      }
    },
    lineColor: '#060603',
    minorTickInterval: null,
    tickColor: '#060603',
    tickWidth: 1,
    title: {
      style: {
        color: 'black',
        fontWeight: 'bold',
        fontSize: '12px',
        fontFamily: 'Trebuchet MS, Verdana, sans-serif'
      }
    }
  },
  tooltip: {
    backgroundColor: 'rgba(0, 0, 0, 0.75)',
    style: {
      color: '#F0F0F0'
    }
  },
  toolbar: {
    itemStyle: {
      color: '#08080C'
    }
  },
  plotOptions: {
    line: {
      dataLabels: {
        color: 'black'
      },
      marker: {
        lineColor: '#08080C'
      }
    },
    spline: {
      marker: {
        lineColor: '#08080C'
      }
    },
    scatter: {
      marker: {
        lineColor: '#08080C'
      }
    },
    candlestick: {
      lineColor: 'black'
    }
  },
  legend: {
    itemStyle: {
      font: '9pt Trebuchet MS, Verdana, sans-serif',
      color: '#000'
    },
    itemHoverStyle: {
      color: '#0B0D0F'
    },
    itemHiddenStyle: {
      color: '#AFAFAF'
    }
  },
  credits: {
    style: {
      color: '#666'
    }
  },
  labels: {
    style: {
      color: 'black'
    }
  },

  navigation: {
    buttonOptions: {
      symbolStroke: '#DDDDDD',
      hoverSymbolStroke: '#FFFFFF',
      theme: {
        fill: {
          linearGradient: {
            x1: 0,
            y1: 0,
            x2: 0,
            y2: 1
          },
          stops: [ [ 0.4, '#9090D3' ], [ 0.6, '#5151A6' ] ]
        },
        stroke: '#C5C5E9'
      }
    }
  },
  rangeSelector: {
    buttonTheme: {
      fill: {
        linearGradient: {
          x1: 0,
          y1: 0,
          x2: 0,
          y2: 1
        },
        stops: [ [ 0.4, '#C5C5E9' ], [ 0.6, '#8D8DE4' ] ]
      },
      stroke: '#C5C5E9',
      style: {
        color: 'white',
        fontWeight: 'bold'
      },
      states: {
        hover: {
          fill: {
            linearGradient: {
              x1: 0,
              y1: 0,
              x2: 0,
              y2: 1
            },
            stops: [ [ 0.4, '#A9A9EB' ], [ 0.6, '#6363E4' ] ]
          },
          stroke: '#C5C5E9',
          style: {
            color: 'white'
          }
        },
        select: {
          fill: {
            linearGradient: {
              x1: 0,
              y1: 0,
              x2: 0,
              y2: 1
            },
            stops: [ [ 0.1, '#1D1D8C' ], [ 0.3, '#3838B9' ] ]
          },
          stroke: '#C5C5E9',
          style: {
            color: '#F2F2FA'
          }
        }
      }
    },
    inputStyle: {
      backgroundColor: '#333',
      color: '#F8F8FC'
    },
    labelStyle: {
      color: '#F8F8FC'
    }
  },

  navigator: {
    handles: {
      backgroundColor: '#58589B',
      borderColor: '#C5C5E9'
    },
    outlineColor: '#A7A7DD',
    maskFill: 'rgba(40, 40, 95, 0.5)',
    series: {
      color: '#7798BF',
      lineColor: '#A6C7ED',
      xAxis: {
        labels: {
          style: {
            color: '#F8F8FC'
          }
        }
      }
    }
  },

  scrollbar: {
    barBackgroundColor: {
      linearGradient: {
        x1: 0,
        y1: 0,
        x2: 0,
        y2: 1
      },
      stops: [ [ 0.4, '#9090D3' ], [ 0.6, '#6666D1' ] ]
    },
    barBorderColor: '#C5C5E9',
    buttonArrowColor: '#C5C5E9',
    buttonBackgroundColor: {
      linearGradient: {
        x1: 0,
        y1: 0,
        x2: 0,
        y2: 1
      },
      stops: [ [ 0.4, '#9090D3' ], [ 0.6, '#6666D1' ] ]
    },
    buttonBorderColor: '#C5C5E9',
    rifleColor: '#FFF',
    trackBackgroundColor: {
      linearGradient: {
        x1: 0,
        y1: 0,
        x2: 0,
        y2: 1
      },
      stops: [ [ 0, '#6666D1' ], [ 1, '#9090D3' ] ]
    },
    trackBorderColor: '#666'
  },

  // special colors for some of the
  legendBackgroundColor: 'rgba(0, 0, 0, 0.5)',
  background2: 'rgb(35, 35, 70)',
  dataLabelsColor: '#C6C6E3',
  textColor: '#C0C0C0',
  maskColor: 'rgba(255,255,255,0.3)'
};
