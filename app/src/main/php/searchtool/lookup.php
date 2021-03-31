<?php
   require '../standard/connect.php';

   $gRating = mysqli_real_escape_string($con, $_POST['gRating']);
   $diameter = mysqli_real_escape_string($con, $_POST['diameter']);
   $result  = mysqli_query($con,"SELECT location FROM products WHERE grating='$gRating' and diameter='$diameter'");
   $row = mysqli_fetch_array($result);
   $data = $row[0];

if ($data) { 
 echo $data;
}

 else {
 	echo "No Meter Found";
}

   mysqli_close($con);
?>