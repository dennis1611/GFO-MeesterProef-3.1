<?php
   require '../standard/connect.php';

   $result = mysqli_query($con,"SELECT DISTINCT grating FROM products ORDER BY grating ASC");

while($row = mysqli_fetch_array($result)){
   $data = $row[0];

if ($data) { 
 echo $data. ",";
}

 else {
 	echo "failed";
}

}

   mysqli_close($con);
?>