<?php
   require '../standard/connect.php';

   $gRating = mysqli_real_escape_string($con, $_POST['grating']);

   $result = mysqli_query($con,"SELECT diameter FROM products WHERE grating='$gRating'");

while($row = mysqli_fetch_array($result)){
   $data = $row[0];

if ($data) { 
 echo $data. ",";
}

 else {
 	echo "No Possible Diameter";
}

}

   mysqli_close($con);
?>