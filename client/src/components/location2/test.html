<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Road API</title>
</head>
<body>
    <h1>Test</h1>
    <div id="map_div"></div>
    <br /><br /><br />
    <hr>
    <br /><br /><br />
	<div id="map_div2"></div>
	<div id="divResult" style="height:200px; padding-left:20px; overflow:auto;"></div>
	
	<script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
	<script src="https://apis.openapi.sk.com/tmap/jsv2?version=1&format=javascript&appKey=l7xx49ec35203c51459ab998a98895d8d47c"></script>
	<script>
		var DrawLine = DrawLine || {}; // NameSpace
		
		DrawLine.CNT_BUFF = 1; // 매끄러운 매칭을 위한 버퍼 포인트 개수
		DrawLine.SPLIT_VALUE = 20; // Road API 로 한번에 요청할 포인트 개수. 최대 100개 까지 가능  ex)샘플코드에서는 SPLIT_VALUE 를 10으로 가정했을 때 요청 포인트 개수가 총 25개라면 API 가 3번 호출됨
		DrawLine.REQ_LIMIT_PER_SEC = 1; // API 초당 요청 제한건수
		
		DrawLine.map = null; // 이동한도로찾기 API 사용전 라인 표출할 지도
   		DrawLine.map2 = null; // 이동한도로찾기 API 사용후 라인 표출할 지도
   		DrawLine.vectorLayer = null; // 이동한도로찾기 API 사용전 벡터 레이어
   		DrawLine.vectorLayer2 = null; // 이동한도로찾기 API 사용후 벡터 레이어
		DrawLine.arrPoint = null; // 포인트 배열 (resource)
		
		DrawLine.totDistance = 0; // 매칭된 거리 (단위: m)
		DrawLine.totPointCount = 0; // 매칭된 좌표의 개수 (단위: count)
		DrawLine.arrMatchedId = []; // 매칭된 링크 아이디 (중복제거)
		
		DrawLine.currentIndex = 0;
		DrawLine.startSourceIndex = 0; // 버퍼 포인트(이전 포인트의 일부를 포함하여 요청)를 제외한 실제 포인트 시작 인덱스
		
		DrawLine.cntReqApi  = 0; // API 요청횟수
		DrawLine.lastMatchedLocation = null; // 이전 마지막 포인트를 저장( 이후 포인트와 연결하기 위함 )
		
		DrawLine.vector_layer = null; // 요청 좌표를 표시할 레이어(매칭 전 지도에 표시)
   		DrawLine.markerLayer = null; // 보간점 레이어
   		DrawLine.marker  = null; // 보간점 마커 
		
		/**
		* 문서 로드 완료시 작업
		*/
		$(function(){
			DrawLine.initMap(); // 지도 그리기
			DrawLine.initData(); // 포인트 데이터 초기화
			DrawLine.setMapBound(); // 포인트에 따른 지도 바운드 설정
			if( DrawLine.isValidate() ) {
				// 유효성 검사를 통과 했다면 할 작업
				DrawLine.log("- LoadApi 요청 작업 시작 -");
				DrawLine.log('------------------------------------------');
				setTimeout(function() { // 지도를 띄우고 API 를 호출하기 위해 딜레이 줌
					DrawLine.splitPoint(); // 포인트 데이터 나눠서 요청하기 ( LoadApi 한번에 요청 가능한 개수 100개 제한 )
				}, 1000);
			}
		});
		
		/**
		* 유효성검사 
		*/
		DrawLine.isValidate = function() {
			if( DrawLine.arrPoint.length <= (DrawLine.CNT_BUFF*2) || DrawLine.SPLIT_VALUE <= DrawLine.CNT_BUFF || DrawLine.SPLIT_VALUE > 100 ) {
				alert("리소스 포인트 데이터 개수는 버퍼크기 초과여야 하고, \'DrawLine.SPLIT_VALUE\' 는 버퍼 크기 초과 100 이하 여야 합니다.");
				return false;
			}
			return true;
		}
		
		/**
   		* 지도 초기화
   		*/
   		DrawLine.initMap = function() {
   			//이동한도로찾기 API 사용전 지도 생성
   			DrawLine.map = new Tmapv2.Map("map_div", {
   				width : "70%",
   				height : "350px",
   				zoomControl : true,
   				scrollwheel : true
   			});
   			
   			//이동한도로찾기 API 사용할 지도 생성
   			DrawLine.map2 = new Tmapv2.Map("map_div2", {
   				width : "70%",
   				height : "350px",
   				zoomControl : true,
   				scrollwheel : true
   			});
   			
   			var agent = navigator.userAgent.toLowerCase();
   			
   		}
		
		/**
		* 포인트 개수 나누기 ( LoadApi request limit 100 )
		*/
		DrawLine.splitPoint = function() {
			var cntAllPoint = DrawLine.arrPoint.length; // 포인트 배열 갯수 구하기
			var pointString = ""; // LoadApi 에 요청할 포인트 스트링
			var arrMatchedPoint = {}; // response 결과
			var i, j, k, cntPointString = 0;
			
			for( i=DrawLine.currentIndex; i < cntAllPoint; i+=2 ) {
				// 포인트 스트링 만들기
				// 경도와 위도 사이는 ‘,’ 좌표와 좌표 사이는 ‘|’ 로 구분 지어 요청 합니다.
				if( pointString != "" ) {
					pointString += '|';
				}
				pointString += DrawLine.arrPoint[i] + ',' + DrawLine.arrPoint[i+1]; // ex) 127.925710,37.557086|127.954464,37.556542
				cntPointString++; // 포인트 스트링 개수 카운트
				
				var coord = new Tmapv2.LatLng(DrawLine.arrPoint[i+1], DrawLine.arrPoint[i]);
    				
   				var circle = new Tmapv2.Circle({	// 원 생성
   					center : coord,
   					radius : 20,
   					fillColor : "#000000",
   					fillOpacity : 0.2,
   					strokeColor : "#000000",
   					strokeWeight : 1,
   					map : DrawLine.map
   				}); 
   				
   				var circle2 = new Tmapv2.Circle({	// 원 생성2
   					center : coord,
   					radius : 20,
   					fillColor : "#FF0000",
   					fillOpacity : 0.2,
   					strokeColor : "#000000",
   					strokeWeight : 1,
   					map : DrawLine.map2
   				}); 
   				
				if( cntPointString == DrawLine.SPLIT_VALUE || (i+2) >= cntAllPoint ) {
					// 포인트 개수가 제한 수에 도달했다면 || 반복문의 마지막 항목 이라면 할 작업 
					
					// 0. LoadApi 요청
					DrawLine.reqLoadApi(pointString, function(response){
						DrawLine.cntReqApi++; // API 요청횟수 카운트
					
						// LoadApi Response 받은 후 작업
						var matchedId = ""; // 매칭된 아이디
						var objNextMatchedLocation = {}; // 바로 직전에 매칭된 좌표
						var objSourceLocation = {}; // 요청한 좌표
						var lastSourceIndex = -1; // 요청 포인트 인덱스 번호
						var arrPointForLine = []; // 선으로 그려질 포인트
						var arrPointForMarker = []; // 마커로 그려질 포인트
						var arrPointForCalDistance = []; // 거리 계산을 위한 포인트
						
						// 결과 값이 존재한다면 실행할 작업
						if( response && response.resultData.matchedPoints ) {
							arrMatchedPoint = response.resultData.matchedPoints; // 매칭된 정보 데이터(matchedPoints)를 arrMatchedPoint 배열에 담는다.
							for( j=0; j < arrMatchedPoint.length; j++ ) {
								objMatchedLocation = arrMatchedPoint[j].matchedLocation;
								objSourceLocation = arrMatchedPoint[j].sourceLocation;
								
								// 1. 매칭 아이디 추가 (중복제거)
								//----------------------------------------------------------------
								matchedId = arrMatchedPoint[j].linkId + "_" + arrMatchedPoint[j].idxName;
								for( k=0; k < DrawLine.arrMatchedId.length; k++ ) {
									if( DrawLine.arrMatchedId[k] == matchedId ) {
										break;
									}
								}
								if( k >= DrawLine.arrMatchedId.length ) {
									// 중복된 아이디가 존재하지 않는다면 할 작업
									DrawLine.arrMatchedId.push( matchedId ); // 매칭된 아이디 목록에 추가
								}
								//----------------------------------------------------------------
								
								if( arrMatchedPoint[j].sourceIndex >= 0 ) {
									// sourceIndex 가 존재한다면 마지막 sourceIndex 갱신
									lastSourceIndex = arrMatchedPoint[j].sourceIndex;
								}
								
								// 2-1. 라인으로 그려질 소스 포인트 리스트 만들기
								if( objSourceLocation ) {
									arrPointForMarker.push(new Tmapv2.LatLng(objSourceLocation.latitude, objSourceLocation.longitude)); // 좌표변환
								}
								
								// 3-1. 라인으로 그려질 매칭 포인트 리스트 만들기
								// 한번에 모든 좌표를 요청하면 문제가 없겠지만 100개 이상의 좌표를 여러번 나눠서 요청해야할 경우 요청과 요청 사이의 매칭된 링크가 어긋날 수 있다.
								// 이를 보정하기 위해 이전 요청 좌표의 일부(버퍼)를 함깨 요청하고 매 요청의 곂치는 부분의 결과를 (버퍼사이즈/2 만큼)덜 그림으로써 좀더 매끄러운 결과를 얻을 수 있다.
								if( arrPointForLine.length == 0 && DrawLine.lastMatchedLocation ) {
									// 이전 요청의 마지막 매칭좌표가 존재한다면 현재 매칭 좌표라인의 맨 앞에 추가 ( 이전 요청 라인과 이어지게 하기 위함 )
									arrPointForLine.push(new Tmapv2.LatLng(DrawLine.lastMatchedLocation.latitude, DrawLine.lastMatchedLocation.longitude)); // 좌표변환
									arrPointForCalDistance.push( DrawLine.lastMatchedLocation ); // 거리 계산을 위해 저장
									DrawLine.lastMatchedLocation = null;
								}
								if( cntAllPoint/2 <= DrawLine.SPLIT_VALUE ) {
									// 1) 처음이자 마지막 요청이라면(전체 요청 좌표개수가 분할요청 기준보다 적다면) => 매칭된 좌표를 모두 라인으로 그림
									if( objMatchedLocation ) {
										arrPointForLine.push(new Tmapv2.LatLng(objMatchedLocation.latitude, objMatchedLocation.longitude)); // 좌표변환
										arrPointForCalDistance.push( objMatchedLocation ); // 거리 계산을 위해 저장
									}
								}
								else if( DrawLine.cntReqApi == 1 ) {
									// 2) 처음 요청이면서 이후에 요청이 있을 예정이라면 => 뒤쪽좌표 중 버퍼의 절반 만큼 그리지 않음
									if( objMatchedLocation && lastSourceIndex < (cntPointString-(DrawLine.CNT_BUFF/2)) ) {
										arrPointForLine.push(new Tmapv2.LatLng(objMatchedLocation.latitude, objMatchedLocation.longitude)); // 좌표변환
										DrawLine.lastMatchedLocation = objMatchedLocation; // 이후 API 요청결과와 라인을 이어가기 위해 마지막 포인트 저장
										arrPointForCalDistance.push( objMatchedLocation ); // 거리 계산을 위해 저장
									}
								}
								else if( DrawLine.cntReqApi > 1 && (i+2) >= cntAllPoint ) {
									// 3) 처음이 아니면서 마지막 API 요청이라면 => 앞쪽좌표 중 버퍼의 절반 만큼 그리지 않음
									if( objMatchedLocation && lastSourceIndex >= (DrawLine.CNT_BUFF/2) ) {
										arrPointForLine.push(new Tmapv2.LatLng(objMatchedLocation.latitude, objMatchedLocation.longitude)); // 좌표변환
										DrawLine.lastMatchedLocation = objMatchedLocation; // 이후 API 요청결과와 라인을 이어가기 위해 마지막 포인트 저장
										arrPointForCalDistance.push( objMatchedLocation ); // 거리 계산을 위해 저장
									}
								}
								else if( DrawLine.cntReqApi > 1 ) {
									// 4) 처음이 아니면서 이후에 API 요청이 있을 예정이라면 => 앞쪽좌표 중 버퍼의 절반, 뒤쪽좌표 중 버퍼의 절반 만큼 그리지 않음
									if( objMatchedLocation && lastSourceIndex >= (DrawLine.CNT_BUFF/2) && lastSourceIndex < (cntPointString-(DrawLine.CNT_BUFF/2)) ) {
										arrPointForLine.push(new Tmapv2.LatLng(objMatchedLocation.latitude, objMatchedLocation.longitude)); // 좌표변환
										DrawLine.lastMatchedLocation = objMatchedLocation; // 이후 API 요청결과와 라인을 이어가기 위해 마지막 포인트 저장
										arrPointForCalDistance.push( objMatchedLocation ); // 거리 계산을 위해 저장
									}
								}
							}
							// 2-2. 소스 포인트 리스트로 라인 그리기
							// 요청한 좌표를 사용해 라인을 그려줍니다.(빨간색)
							DrawLine.drawLine( arrPointForMarker, "#FF0000" );
							
							// 3-2. 매칭 포인트 리스트로 라인 그리기
							// 요청한 좌표와 매칭되는 좌표를 사용해 라인을 그려줍니다.(파랑색)
							DrawLine.drawLine( arrPointForLine, "#0000FF" );
							
							// 4. 매칭된 좌표의 거리 구하기
							for( k=0; k<(arrPointForCalDistance.length-1); k++ ) {
								DrawLine.totDistance += DrawLine.calDistance(arrPointForCalDistance[k].longitude, arrPointForCalDistance[k].latitude, arrPointForCalDistance[k+1].longitude, arrPointForCalDistance[k+1].latitude);
							}
							
							// 5. 매칭된 좌표의 개수 구하기
							DrawLine.totPointCount += arrPointForCalDistance.length;
							if( DrawLine.cntReqApi > 1 && arrPointForCalDistance.length >= 1 ) {
								// 첫 요청이 아니면서(앞서 요청한 API 의 마지막 좌표가 포함되어 있으면서), 매칭된 포인트가 1개 이상이라면  카운트 1개 뺌
								DrawLine.totPointCount--;
							}

							// 거리 계산용 포인트 리스트 초기화
							arrPointForCalDistance = [];
						}
					});
					
					if( (i+2) < cntAllPoint ) {
						// 마지막 항목이 아니라면 버퍼 포인트 추가
						pointString = ""; // LoadAPI 의 파라미터 초기화
						cntPointString = 0; // 포인트 스트링 개수 초기화(LoadAPI 에 포인트 분할 요청을 위한 카운트)
						i -= (DrawLine.CNT_BUFF*2); // 매끄러운 링크 매칭을 위해 이전 일부 포인트를 포함한 매칭을 한다. (매칭 버퍼)
						DrawLine.startSourceIndex = DrawLine.CNT_BUFF; // 매칭 버퍼 포인트를 제외하고 매칭 좌표 수를 카운트 하기 위한 변수
					}
					DrawLine.currentIndex = i;
					
					DrawLine.log("총 거리 : " + DrawLine.totDistance.toFixed(2) + "m");
					DrawLine.log("매칭된 링크의 개수 : " + DrawLine.arrMatchedId.length + "개");
					DrawLine.log("총 좌표의 개수 : " + DrawLine.totPointCount + "개");
					DrawLine.log('------------------------------------------');
					
					// 중간 진행과정을 화면에 표출하기 위한 작업
					if( (i+2) < cntAllPoint ) {
						DrawLine.currentIndex += 2; // 포인트 이동
						setTimeout( DrawLine.splitPoint, (1000/DrawLine.REQ_LIMIT_PER_SEC) ); // 재귀호출
						break;
					}
					else {
						DrawLine.log("- LoadApi 요청 작업 완료 -");
					}
				}
			}
		}
		
		/**
   		* 라인 그리기
   		*/
   		DrawLine.drawLine = function( pointList, lineColor ) {
   			var polyline_;
   			
   			if(lineColor == "#FF0000"){
   				polyline_ = new Tmapv2.Polyline({
   					path : pointList,
   					strokeColor : lineColor,
   					strokeWeight: 6,
   					map : DrawLine.map
   				});
   			}else{
	   				// 지도상에 그려질 스타일을 설정합니다.
   				polyline_ = new Tmapv2.Polyline({
   					path : pointList,
   					strokeColor : lineColor,
   					strokeWeight: 6,
   					map : DrawLine.map2
   				});
   			}
   		}
		
		/**
   		* 로드 매칭 API 요청
   		*/
   		DrawLine.reqLoadApi = function( pointString, callback ) {
   			var url = 'https://apis.openapi.sk.com/tmap/road/matchToRoads?version=1&appKey=l7xx49ec35203c51459ab998a98895d8d47c'; // 이동한 도로 찾기 api 요청 url입니다.
   			
   			$.ajax({
   			     type: 'POST',
			  	 contentType: "application/x-www-form-urlencoded",
   			     url: url,
   			     data: {
		    			"responseType" : "1", // 1:전체 데이터 요청, 2:요청좌표 및 매치된 좌표를 제외한 데이터 요청
		    			 "coords" : pointString // 좌표계는 WGS84GEO, 매핑에 사용될 좌표 목록입니다.  경도와 위도 사이는 "," 좌표와 좌표 사이에는 "|"로 구분지어 요청합니다. 
			  			},
   			     async: false,
   			     success: function(data) {
   			  	   callback(data);
   			     }
   			});
   		}
		
		/**
		* 좌표가 한눈에 들어오는 바운드 찾기
		*/
		DrawLine.setMapBound = function() {
			var positionBounds = new Tmapv2.LatLngBounds(); // bounds 인스턴스를 생성합니다.
			var i;
			
			for( i=0; i < DrawLine.arrPoint.length; i+=2 ) {  // 포인트 배열 (resource)의 길이만큼 반복
				// 좌표변환 후 bounds 확장
				var boundPosition = new Tmapv2.LatLng(DrawLine.arrPoint[i+1], DrawLine.arrPoint[i]);
    			positionBounds.extend(boundPosition);
			}
			DrawLine.map.panToBounds(positionBounds); // 매칭전 좌표가 한눈에 들어올 수 있는 지도 중심과 줌레벨 설정
   			DrawLine.map2.panToBounds(positionBounds); // 매칭후 좌표가 한눈에 들어올 수 있는 지도 중심과 줌레벨 설정
		}
		
		/**
		* 로그 표시
		*/
		DrawLine.log = function( msg ) {
			$('#divResult').html( $('#divResult').html() + msg + "<br/>" );
			$("#divResult").scrollTop($("#divResult")[0].scrollHeight); // 스크롤 맨 아래로 이동
		}
		
		/**
		* 위경도로 거리 구하기
		*/
		DrawLine.calDistance = function( lon1, lat1, lon2, lat2 ){
		    var theta, dist;
		    
		    if( lon1 == lon2 && lat1 == lat2 )
		    	return 0;
		    
		    theta = lon1 - lon2;
		    dist = Math.sin(DrawLine.deg2rad(lat1)) * Math.sin(DrawLine.deg2rad(lat2)) + Math.cos(DrawLine.deg2rad(lat1))
		          * Math.cos(DrawLine.deg2rad(lat2)) * Math.cos(DrawLine.deg2rad(theta));
		    dist = Math.acos(dist);
		    dist = DrawLine.rad2deg(dist);
		    
		    dist = dist * 60 * 1.1515;
		    dist = dist * 1.609344; // 단위 mile 에서 km 변환
		    dist = dist * 1000.0; // 단위  km 에서 m 로 변환
		    
		    return Number(Number(dist).toFixed(2));
		}
		
		/**
		* 주어진 도(degree) 값을 라디언으로 변환
		*/
		DrawLine.deg2rad = function( deg ){
		    return (deg * Math.PI / 180);  
		}  
		
		/**
		* 주어진 라디언(radian) 값을 도(degree) 값으로 변환
		*/
		DrawLine.rad2deg = function( rad ){  
		    return (rad * 180 / Math.PI);  
		} 
		 
		/*
		* 이동한도로찾기 요청시 1회 좌표 100개까지 지원가능합니다. 
   		*/
   		/*포인트 배열 초기화*/
   		DrawLine.initData = function() {
   			DrawLine.arrPoint = [
               128.62097089126576,35.89315725638313,
                128.6221513571504,35.892643447504284,
                128.62245966742682,35.89249624765208,
                128.6232068338938,35.89213519124339
   			];
   		}
		
	</script>
</body>
</html>