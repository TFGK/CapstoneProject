<?php
$con = mysqli_connect("54.180.103.20","nabe","nabe","eyes");
if(mysqli_connect_errno($con)){
    echo "Failed to connect to MySQL".mysqli_connect_error();
}
mysqli_set_charset($con,"utf8");

$res = mysqli_query($con,"select * from locations");
$result = array();

while($row = mysqli_fetch_array($res)){
    array_push($result,
              array('id'=>$row[0],'location_name'=>$row[1],'location_lat'=>$row[3],
              'location_lng'=>$row[4]));
}

echo json_encode(array("result"=>$result));
mysqli_close($con);
?>
