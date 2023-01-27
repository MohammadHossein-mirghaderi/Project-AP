public class AdResponse {
     private String message;
     private boolean success;
     private Ad ad;
 
     public AdResponse(String message, boolean success, Ad ad) {
         this.message = message;
         this.success = success;
         this.ad = ad;
     }
 
     public String getMessage() {
         return message;
     }
 
     public void setMessage(String message) {
         this.message = message;
     }
 
     public boolean isSuccess() {
         return success;
     }
 
     public void setSuccess(boolean success) {
         this.success = success;
     }
 
     public Ad getAd() {
         return ad;
     }
 
     public void setAd(Ad ad) {
         this.ad = ad;
     }
 }
 