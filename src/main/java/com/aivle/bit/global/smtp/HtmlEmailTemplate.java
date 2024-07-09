package com.aivle.bit.global.smtp;

import lombok.RequiredArgsConstructor;

class HtmlEmailTemplate {

    private static final String BASIC_FORM = """
            <table border="0" cellpadding="0" cellspacing="0" width="100%%" id="bodyTable" style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
               <tr>
                 <td align="center">
                   <table border="0" cellpadding="0" cellspacing="0" width="600" style="border: 1px solid #ddd; padding: 20px; background-color: #ffffff; border-top: 10px solid #f76c5e; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
                     <tr>
                       <td style="text-align: center; padding-bottom: 20px;">
                         <img src="https://storage.googleapis.com/32_bit/logo.png" alt="재무탐정 LOGO" width="95" height="95" />
                       </td>
                     </tr>
                     %s
                     <tr>
                       <td style="padding-top: 18px; border-top: 1px solid #eeeeee;">
                         <hr style="border: none; border-top: 1px solid #eeeeee; margin: 20px 0;">
                       </td>
                     </tr>
                     <tr>
                       <td style="padding-top: 12px;">
                         <div style="font-size: 14px; color: #555555;">
                           문의사항이 있다면 다음 이메일로 연락 바랍니다!<br/>
                           E-mail: <a href="mailto:pwrwpw.dev@gmail.com" style="color: #f76c5e; text-decoration: none;">pwrwpw.dev@gmail.com</a>
                         </div>
                       </td>
                     </tr>
                   </table>
                 </td>
               </tr>
             </table>
        """;

    @RequiredArgsConstructor
    enum MailType {
        EMAIL_VERIFICATION("[재무탐정] 이메일 인증을 진행해 주세요!", """
            <tr>
              <td style="padding-top: 20px;">
                <h1 style="font-size: 24px; color: #333333; margin: 0;">이메일 인증을 진행해 주세요!</h1>
              </td>
            </tr>
            <tr>
              <td style="padding-top: 10px; padding-bottom: 20px;">
                <p style="font-size: 16px; color: #333333;">이메일 인증 코드는 다음과 같습니다:</p>
                <p style="font-size: 24px; color: #f76c5e; font-weight: bold;">%s</p>
              </td>
            </tr>
            """);

        private final String subject;
        private final String content;

        public String content(String code) {
            return wrapBasicFrom(content.formatted(code));
        }

        private String wrapBasicFrom(String content) {
            return BASIC_FORM.formatted(content);
        }

        public String subject() {
            return subject;
        }
    }

    private HtmlEmailTemplate() {
    }

}