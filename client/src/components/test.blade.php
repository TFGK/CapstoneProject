<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <style>
        html,
        body,
        #google-map {
            width: 1000px;
            height: 720px;
            margin: 5;
            top: 10%;
            left: 10%;
            padding: 0;
        }

        #search-panel {
            position: absolute;
            left: 6%;
            top: 3%;
            z-index: 5;
            background-color: #FFFFFF;
            padding: 5px;
            border: 1px solid black;
            text-align: center;
            padding-left: 10px
        }

        /* marker tool bar */
        #floating-panel {
            position: absolute;
            left: 35%;
            top: 3%;
            z-index: 5;
            background-color: #FFFFFF;
            padding: 5px;
            border: 1px solid black;
            text-align: center;
            /* font-family: 'Roboto', 'sans-serif'; */
            /* line-height: 30px; */
            padding-left: 10px;
        }

        #location_save {
            position: absolute;
            left: 75%;
            top: 20%;
            z-index: 5;
            background-color: #FFFFFF;
            padding: 5px;
            border: 1px solid black;
            text-align: center;
            padding-left: 10px
        }
    </style>
    <title></title>
</head>

<body>
    <div id="search-panel">
        <input id="address" type="text" value="주소를 입력하세요" />

        <button id="submit" type="button" value="Geocode">지도 검색</button>
    </div>
    <!-- remove maker code -->
    <div id="floating-panel">
        <input type="button" onclick="clearMarkers();" value="마커 숨기기"/>
        <input type="button" onclick="showMarkers();" value="모든 마커 보이기"/>
        <input type="button" onclick="deleteMarkers();" value="마커 모두 삭제"/>
        <input type="button" onclick="deleteMarkersOne();" value="마커 한개 삭제"/>
        <input type="button" onclick="showallMaker();" value="DB마커 불러오기"/>
    </div>

    <div id="google-map">
    </div>
    <!-- saved data -->
    <form id="location_save" action="/locations" method="POST">
        @csrf
        장소 명 : <input type="text" name="location_name" id="location_name"/><br/>
        장소 설명 : <input type="text" name="location_explain" id="location_explain"/><br/>
        위도 : <input type="text" name="location_lat" id="location_lat"/><br/>
        경도 : <input type="text" name="location_lng" id="location_lng"/><br/>
        <p>
            <input type="submit" value="save" />
        </p>
    </form>
    <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    <!-- Google Map API -->
    <script src="https://unpkg.com/@google/markerclustererplus@4.0.1/dist/markerclustererplus.min.js"></script>
    <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDHdsRLRYeFeHSLBI5SuaNw1Op98vcFsDM"></script>
    <script>
        var map;
        var markers = [];
        var locations = [];

        $(document).ready(function(){
            $.ajax({ 
                url: "/locations/load",
                method:"GET",
                },)
                .then((data) => {
                    $.each(data, function(index, item){
                        locations.push({lat:parseFloat(item.lat), lng:parseFloat(item.lng)})
                        // console.log(item.lat, item.lng)
                    })
                })
                .then(()=>{
                    initMap()
                })
        })
        // AIzaSyA8Acop1PDTra-V8pBC19IzMSNhqvP9Z20
        function initMap() {
            console.log('Map is initialized.');
            console.log(typeof locations)
            console.log(locations)
            /**
             * 맵을 설정한다.
             * 1번째 파라미터 : 구글 맵을 표시할 위치. 여기서는 #google-map
             * 2번째 파라미터 : 맵 옵션.
             */

            map = new google.maps.Map(document.getElementById('google-map'), {
                zoom: 7,
                center: {lat: 35.893556128, lng: 128.61981}
            });
            var labels = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
            
            markers = locations.map(function(location, i) {
                return new google.maps.Marker({
                    position: location,
                    label: labels[i % labels.length]
                });
            });
            
            var markerCluster = new MarkerClusterer(map, markers,
            {imagePath: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m'});
            
            map.addListener('click', function(event) {
                console.log(event)
                console.log(event.latLng)
                addMarker(event.latLng);
            });
        }

        //geocodeAddress : 입력한 주소로 맵의 좌표를 바꾼다.
        function geocodeAddress(geocoder, resultMap) {
            console.log('geocodeAddress 함수 실행');

            // 주소 설정
            var address = document.getElementById('address').value;

            /**
             * 입력받은 주소로 좌표에 맵 마커를 찍는다.
             * 1번째 파라미터 : 주소 등 여러가지. 
             *      ㄴ 참고 : https://developers.google.com/maps/documentation/javascript/geocoding#GeocodingRequests
             * 
             * 2번째 파라미터의 함수
             *      ㄴ result : 결과값
             *      ㄴ status : 상태. OK가 나오면 정상.
             */
            geocoder.geocode({
                'address': address
            }, function(result, status) {
                console.log(result);
                console.log(status);

                if (status === 'OK') {
                    // 맵의 중심 좌표를 설정한다.
                    resultMap.setCenter(result[0].geometry.location);
                    // 맵의 확대 정도를 설정한다.
                    resultMap.setZoom(18);
                    // 맵 마커
                    var marker = new google.maps.Marker({
                        map: resultMap,
                        position: result[0].geometry.location
                    });

                } else {
                    alert('지오코드가 다음의 이유로 성공하지 못했습니다 : ' + status);
                }
            });
        }

        // Adds a marker to the map and push to the array.
        function addMarker(location) {
            var marker = new google.maps.Marker({
                position: location, 
                map: map
            });
            
            var lat = marker.position.lat();
            var lng = marker.position.lng();
            console.log('위도 : ' + lat);
            console.log('경도 : ' + lng);
            
            markers.push(marker);

            document.getElementById('location_lat').value = lat;
            document.getElementById('location_lng').value = lng;
        }
            // 클릭 시 input 박스에 위도 경로 표시
        // Sets the map on all markers in the array.
        function setMapOnAll(map) {
        }
            for (var i = 0; i < markers.length; i++) {
                markers[i].setMap(map);
        }
        
        setMapOnAll(null);
        // Removes the markers from the map, but keeps them in the array.
        function clearMarkers() {
        }
        
        setMapOnAll(map);
        // Shows any markers currently in the array.
        function showMarkers() {
        }
        
        // Deletes all markers in the array by removing references to them.
        function deleteMarkers() {
            clearMarkers();
            markers = [];
        }

        function showallMaker() {
            $.ajaxSetup({
                headers: {
                    'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content')
                }
            });
            $.ajax({
                    type: 'GET',
                    url: '/map',
                })
                .then((data) => {
                    console.log('show에 들렸다가 데이터 가져왔음');
                    console.log(data);
                    $('#Mymodal').css("display", "block");
                    $('#photo_preview').attr("src", "{{ URL::to('/') }}/img/" + data.image); // 예전 사진 로딩

                    $('#name').val(data.name); // 예전 이름 로딩
                    $('#comment').val(data.comment); // 예전 코멘트 로딩
                    
                    $('#hidden_id').val(data.id);
                });
            }
            var leng = markers.length - 1;
            
            
            function deleteMarkersOne() {
                if (leng > -1) {
            markers[leng].setMap(null);
            console.log(markers);
                markers.splice(leng, 1);
            }
            console.log(markers);
        }

        function save() {
            var lat = marker.position.lat(); // 위도
            var lng = marker.position.lng(); // 경도
        }
    </script>
</body>

</html>