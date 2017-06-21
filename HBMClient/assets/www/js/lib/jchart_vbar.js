var jchart_vbar = function(){

  var width = window.innerWidth;

  var padding = {left:5,right:20,top:20,bottom:20};
    
  var xdataset = [], ydataset = [], _IDp;
    
  var rectPadding = 4;

  var anchor=["用户数(单位:户)"];
    
function randomData() {
    return Math.random() * -1000000000 | 0 ;  //10 以内数字取整
}

function _update() {

    ydataset.length = 0;
    for (var j = 0; j < 8; ++j)
        ydataset.push(randomData());    

    $( '#' + _IDp ).empty();
    $( '#' + _IDp ).append('<p><a></a><span></span></p>');
    
    if( d3.max(ydataset) == 0 &&  d3.min(ydataset) ==0 ){
        console.log( '---' );
    }else{
        if(d3.max(ydataset) <= 0 ){
            _renderminus(200);    
        }else if( d3.min(ydataset) > 0 ){
            _renderplus(200);
        }else{
            _renderall( 400 );
        }
    }
}
    
function _init(IDp,tpara,xdata,ydata){
    
    xdataset = xdata;
    ydataset = ydata;
    _IDp = IDp;
    
    anchor.length = 0;
    anchor.push( tpara );
    
    $( '#' + _IDp ).empty();
    $( '#' + _IDp ).append('<p><a></a><span></span></p>');
    
    if( (d3.max(ydataset) == 0 &&  d3.min(ydataset) ==0)|| ydataset.length ==0 ){
        console.log( '数据为空' );
    }else{
        if(d3.max(ydataset) <= 0 ){
            _renderminus(200);    
        }else if( d3.min(ydataset) >= 0 ){
            _renderplus(200);
        }else{
            _renderall( 400 );
        }
    }
}

function _renderminus( _height ){
    padding.top = 40;
    var vmax;
    if( d3.min(ydataset) != d3.max(ydataset) ){
        
        if( (d3.max(ydataset) - d3.min(ydataset))/2 < 0-d3.max(ydataset)*2/5){
            vmax = d3.max(ydataset) + (d3.max(ydataset) - d3.min(ydataset))*2/5
        }else{
            vmax = d3.max(ydataset)*3/5;
        }
        
    }else{
        vmax = 0;
    }
    
	var svg = d3.select('#' + _IDp )
		.append("svg")
		.attr("width", width)
		.attr("height", _height );

	var xScale = d3.scale.ordinal()
		.domain(d3.range(ydataset.length))
		.rangeRoundBands([0, width - padding.left - padding.right]);

    var yScale = d3.scale.linear()
            .domain([d3.min(ydataset),vmax])
            .range([(_height - padding.top - padding.bottom),0]);

	var xAxis = d3.svg.axis()
		.scale(xScale)
		.orient("top")
        .tickFormat(function(v,i){
            return xdataset[i];
        });
		
	var yAxis = d3.svg.axis()
		.scale(yScale)
		.orient("right")
        .ticks(5);
    
	var rectPadding = 4;

	var rects = svg.selectAll(".MyRect")
		.data(ydataset)
		.enter()
		.append("rect")
		.attr("class","MyRect")
		.attr("transform","translate(" + padding.left + "," + padding.top + ")")
		.attr("x", function(d,i){
			return xScale(i) + rectPadding/2;
		} )
		.attr("width", xScale.rangeBand() - rectPadding )
		.attr("y",function(d){
            return 0;
		})
		.attr("height", function(d){
			 return 0;
		})
        .on("tap",function(d,i){
            
            var td,tdv;
            if( d < -100000000 ){
                td = d /100000000;
                td = td.toFixed(2);
                tdv = '亿';
            }else if(  d < -10000 ){
                td = d /10000;
                td = td.toFixed(2);
                tdv = '万';
            }else{
                td = d ;
                td = td.toFixed(2);
            }
            
            var p = d3.select('#' + _IDp)
                        .selectAll("p")
                        .attr("width",width)
                        .attr("style","text-align:center");
            
            var a = d3.select('#' + _IDp)
                        .selectAll("a")
                        .text( td )
                        .attr("style","color:green;font-size:32px");   
            
            var span = d3.select('#' + _IDp)
                        .selectAll("span")
                        .text( tdv )
                        .attr("style","color:#2573fe;font-size:15px");
            
                p.style("color","green");
                d3.select(this)
				    .attr("fill","green");
            
            d3.select( this )
				.transition()
		        .duration(500)
				.attr("fill","#ff7f50");

		})
		.transition()
		.delay(function(d,i){
			return i * 200;
		})
		.duration(2000)
		.ease("bounce")
		.attr("y",function(d){
                return  0;
		})
		.attr("height", function(d){
                return  yScale(d) ;

		})
        .attr("fill","#ff7f50");
    
    svg.selectAll("text").data(anchor).enter().append('text')
        .attr("x",  padding.left)
        .attr("y",  10)
        .attr("width",width -padding.left )
        .attr("height",30 )
        .attr("style","font-size:12px;text-align:left")
        .text(function(d){return d;});
    
	svg.append("g")
		.attr("class","axis")
		.attr("transform","translate(" + padding.left + "," + padding.top + ")")
		.call(xAxis); 
		
	svg.append("g")
		.attr("class","axis")
		.attr("transform","translate(" + padding.left + "," + padding.top + ")")
		.call(yAxis);
}   
function _renderplus( _height ){
    
    padding.top = 20;
    
    var vmin;
    if( d3.min(ydataset) != d3.max(ydataset) ){
        
        if( (d3.max(ydataset) - d3.min(ydataset))/2 < d3.min(ydataset)*2/5){
            vmin = d3.min(ydataset) - (d3.max(ydataset) - d3.min(ydataset))*2/5
        }else{
            vmin = d3.min(ydataset)*3/5;
        }
        
    }else{
        vmin = 0;
    }
    
	var svg = d3.select('#' + _IDp)
		.append("svg")
		.attr("width", width)
		.attr("height", _height );

	var xScale = d3.scale.ordinal()
		.domain(d3.range(ydataset.length))
		.rangeRoundBands([0, width - padding.left - padding.right]);

    var yScale = d3.scale.linear()
            .domain([vmin,d3.max(ydataset)])
            .range([(_height - padding.top - padding.bottom), 0]);
    
	var xAxis = d3.svg.axis()
		.scale(xScale)
		.orient("bottom")
        .tickFormat(function(v,i){
            return  xdataset[i];
        });
		
	var yAxis = d3.svg.axis()
		.scale(yScale)
		.orient("right")
        .ticks(5);
    
	var rectPadding = 4;

	var rects = svg.selectAll(".MyRect")
		.data(ydataset)
		.enter()
		.append("rect")
		.attr("class","MyRect")
		.attr("transform","translate(" + padding.left + "," + padding.top + ")")
		.attr("x", function(d,i){
			return xScale(i) + rectPadding/2;
		} )
		.attr("width", xScale.rangeBand() - rectPadding )
		.attr("y",function(d){
               var min = yScale.domain()[0];
			    return yScale(min);
		})
		.attr("height", function(d){
			 return 0;
		})
        .on("tap",function(d,i){
            
            var td,tdv;
            if( d > 100000000 ){
                td = d /100000000;
                td = td.toFixed(2);
                tdv = '亿';
            }else if( d > 10000 ){
                td = d /10000;
                td = td.toFixed(2);
                tdv = '万';
            }else{
                td = d ;
                td = td.toFixed(2);
            }
            
            var p = d3.select('#' + _IDp)
                        .selectAll("p")
                        .attr("width",width)
                        .attr("style","text-align:center");
            
            var a = d3.select('#' + _IDp)
                        .selectAll("a")
                        .text( td )
                        .attr("style","color:red;font-size:32px");   
            
            var span = d3.select('#' + _IDp)
                        .selectAll("span")
                        .text( tdv )
                        .attr("style","color:blue;font-size:15px");
            
            d3.select(this)
              .attr("fill","red");
            
            d3.select( this )
				.transition()
		        .duration(500)
				.attr("fill","#ff7f50");

		})
		.transition()
		.delay(function(d,i){
			return i * 200;
		})
		.duration(2000)
		.ease("bounce")
		.attr("y",function(d){
                return yScale(d);
		})
		.attr("height", function(d){
                return (_height - padding.top - padding.bottom) - yScale(d);

		})
        .attr("fill","#ff7f50");
   
     svg.selectAll("text").data(anchor).enter().append('text')
        .attr("x",  padding.left)
        .attr("y",  10)
        .attr("width",width -padding.left )
        .attr("height",30 )
        .attr("style","font-size:12px;text-align:left")
        .text(function(d){return d;});
    
	svg.append("g")
		.attr("class","axis")
		.attr("transform","translate(" + padding.left + "," + (_height - padding.bottom) + ")")
		.call(xAxis); 
		
	svg.append("g")
		.attr("class","axis")
		.attr("transform","translate(" + padding.left + "," + padding.top + ")")
		.call(yAxis);
}
function _renderall( _height ){
    padding.top = 20;
    
	var svg = d3.select('#' + _IDp)
		.append("svg")
		.attr("width", width)
		.attr("height", _height);

	var xScale = d3.scale.ordinal()
		.domain(d3.range(ydataset.length))
		.rangeRoundBands([0, width - padding.left - padding.right]);

    var yScale = d3.scale.linear()
            .domain([0,d3.max(ydataset)])
            .range([(_height - padding.top - padding.bottom)/2 | 0, 0]);
    
    var yScale2 = d3.scale.linear()
            .domain([d3.min(ydataset),0])
            .range([(_height - padding.top - padding.bottom)/2 | 0,0]);
    
	var xAxis = d3.svg.axis()
		.scale(xScale)
		.orient("bottom")
        .tickFormat(function(v,i){
            return xdataset[i];
        });;
		
	var yAxis = d3.svg.axis()
		.scale(yScale)
		.orient("right")
        .ticks(5);

    var yAxis2 = d3.svg.axis()
		.scale(yScale2)
		.orient("right")
        .ticks(5);
    
	var rectPadding = 4;

	var rects = svg.selectAll(".MyRect")
		.data(ydataset)
		.enter()
		.append("rect")
		.attr("class","MyRect")
		.attr("transform","translate(" + padding.left + "," + padding.top + ")")
		.attr("x", function(d,i){
			return xScale(i) + rectPadding/2;
		} )
		.attr("width", xScale.rangeBand() - rectPadding )
		.attr("y",function(d){
            
            if( d>= 0 ){
               var min = yScale.domain()[0];
			    return yScale(min);
            }else{
                var min = yScale2.domain()[0];
			    return yScale2(min);
            }
		})
		.attr("height", function(d){
			 return 0;
		})
        .on("tap",function(d,i){
            
            var td,tdv;

            if( d < -100000000 ){
                td = d /100000000;
                td = td.toFixed(2);
                tdv = '亿';
            }else if(  d < -10000 ){
                td = d /10000;
                td = td.toFixed(2);
                tdv = '万';
            }else if( d < 10000 ){
                td = d ;
                td = td.toFixed(2);
            }else if( d < 100000000 ){
                td = d /10000;
                td = td.toFixed(2);
                tdv = '万';
            }else{
                td = d /100000000;
                td = td.toFixed(2);
                tdv = '亿';
            }
            
            var p = d3.select('#' + _IDp)
                        .selectAll("p")
                        .attr("width",width)
                        .attr("style","text-align:center");
            
            var a = d3.select('#' + _IDp)
                        .selectAll("a")
                        .text( td )
                        .attr("style","color:red;font-size:32px");   
            
            var span = d3.select('#' + _IDp)
                        .selectAll("span")
                        .text( tdv )
                        .attr("style","color:blue;font-size:15px");
            
            if( d < 0 ){
                a.style("color","green");
                d3.select(this)
				    .attr("fill","green");
            }else{
                d3.select(this)
				    .attr("fill","red");
            }
            
            d3.select( this )
				.transition()
		        .duration(500)
				.attr("fill","#ff7f50");

		})
		.transition()
		.delay(function(d,i){
			return i * 200;
		})
		.duration(2000)
		.ease("bounce")
		.attr("y",function(d){
            if(d  >= 0 ){
                return yScale(d);
            }else{
                return  yScale2(yScale2.domain()[0]);
            }
		})
		.attr("height", function(d){
            if( d >= 0 ){
                return (_height - padding.top - padding.bottom)/2 - yScale(d);
            }else{
                 return  yScale2(d) ;
            }
		})
        .attr("fill","#ff7f50");
  
     svg.selectAll("text").data(anchor).enter().append('text')
        .attr("x",  padding.left)
        .attr("y",  10)
        .attr("width",width -padding.left )
        .attr("height",30 )
        .attr("style","font-size:12px;text-align:left")
        .text(function(d){return d;});
    
	svg.append("g")
		.attr("class","axis")
		.attr("transform","translate(" + padding.left + "," + (_height - padding.bottom +padding.top)/2 + ")")
		.call(xAxis); 
		
	svg.append("g")
		.attr("class","axis")
		.attr("transform","translate(" + padding.left + "," + padding.top + ")")
		.call(yAxis);
    
	svg.append("g")
		.attr("class","axis")
		.attr("transform","translate(" + padding.left + "," + _height/2 + ")")
		.call(yAxis2);
}
    
    return {
        update: _update,
        init : _init
    }
}();































