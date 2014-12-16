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
     }
}

module.exports = gpsmode;