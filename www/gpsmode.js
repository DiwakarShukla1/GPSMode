var gpsmode = {
    on: function(successCallback, errorCallback) {
        cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'GPSMode', // mapped to our native Java class called "Calendar"
            'on',
            []
        ); 
     },
     off: function(successCallback, errorCallback) {
        cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'GPSMode', // mapped to our native Java class called "Calendar"
            'off',
            []
        ); 
     },
     isGPSOn: function(successCallback,errorCallback){
       cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'GPSMode', // mapped to our native Java class called "Calendar"
            'isGPSOn',
            []
        ); 
     },
     getLocation:function(successCallback,errorCallback){
        cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'GPSMode', // mapped to our native Java class called "Calendar"
            'getLocation',
            []
        ); 
     }
}

module.exports = gpsmode;