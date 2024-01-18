package dto;


public class MailDto {

    public String mailExpeditor;
    public String[] mailsDestinataires;
    public String object;
    public String content;


    public MailDto(String mailExpeditor, String[] mailsDestinataires, String object, String content) {
        this.mailExpeditor = mailExpeditor;
        this.mailsDestinataires = mailsDestinataires;
        this.object = object;
        this.content = content;
    }


    @Override
    public String toString() {
        return "MailDto {" +
                "from='" + this.getMailExpeditor() + '\'' +
                ", to=" + this.getMailsDestinataires() +
                ", object='" + this.object + '\'' +
                ", content='" + this.content + '\'' +
                '}';
    }


    public String getMailExpeditor() {
        return mailExpeditor;
    }


    public String getContent() {
        return content;
    }


    public String getObject() {
        return object;
    }


    public String[] getMailsDestinataires() {
        return this.mailsDestinataires;
    }

}
