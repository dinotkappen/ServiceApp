package com.app.service.Model;

import android.net.Uri;

public class AttachmentItemsModel {


    String attachmentName;


    public Uri getAttachment() {
        return attachment;
    }

    public void setAttachment(Uri attachment) {
        this.attachment = attachment;
    }

    Uri attachment;




    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentDelete() {
        return attachmentDelete;
    }

    public void setAttachmentDelete(String attachmentDelete) {
        this.attachmentDelete = attachmentDelete;
    }

    String attachmentDelete;

    public AttachmentItemsModel(Uri attachment, String attachmentName,String attachmentDelete) {

      this.attachment=attachment;
        this.attachmentName = attachmentName;
        this.attachmentDelete=attachmentDelete;
    }
}
