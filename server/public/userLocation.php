<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbconn.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
        $userid=$_POST['user_id'];
        $latitude=$_POST['place_lat'];
        $longitude=$_POST['place_lng'];
        $place=$_POST['place_name'];

        if(!isset($errMSG))
        {
            try{
                $stmt = $con->prepare('INSERT INTO timelines(user_id,place_lat,place_lng,place_name) VALUES(:user_id, :place_lat, :place_lng, :place_name)');
                $stmt->bindParam(':user_id', $userid);
                $stmt->bindParam(':place_lat', $latitude);
                $stmt->bindParam(':place_lng', $longitude);
                $stmt->bindParam(':place_name', $place);

                if($stmt->execute())
                {
                    $successMSG = "위치를 추가했습니다.";
                }
                else
                {
                    $errMSG = "추가 에러";
                }

            } catch(PDOException $e) {
                die("Database error: " . $e->getMessage()); 
            }
        }

    }
?>
<?php 
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

   $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
   
    if( !$android )
    {
?>
    <html>
       <body>
            <form action="<?php $_PHP_SELF ?>" method="POST">
                ID: <input type = "text" name = "user_id" />
                위도: <input type = "text" name = "place_lat" />
                경도: <input type = "text" name = "place_lng" />
                장소: <input type = "text" name = "place_name" />
                <input type = "submit" name = "submit" />
            </form>

       </body>
    </html>
<?php 
    }
?>