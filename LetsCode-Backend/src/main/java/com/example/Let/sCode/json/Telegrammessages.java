package com.example.Let.sCode.json;

public enum Telegrammessages {
    
    RandomResp("Select the Platform for generating random question!"),
    SolvedResp("Select the Platform for generating random solved question!"),
    NotEnabled("Sorry, you haven't enabled Telegram notifications on the Let's Code platform.\n You can Enabled it by using [LINK]((%s/profile/%s?edit_page=1)"),
    DailyChallenges("Here are your daily challenges for today!"),
    DailyAutoChallenges("Hi XX! 👋,\nHere are your daily challenges for today!"),
    OtpIncorrectResp("Your OTP is incorrect. Please try again."),
    DailyChallenge("Here is your challenge!\n"),
    Incorrectemail("The Email you provided in the Let'sCode platform does not exist. Update it!"),
    IncorrectPhoneNumber("The phone number you provided in the Let'sCode platform does not exist. Update it!"),
    GenarateOtp("Hello, Coding Enthusiast! Your OTP is 123456. Get ready to receive daily problem-solving challenges via our Telegram bot."),
    OtpResp("Please enter the OTP that was sent to your registered email in 100 Seconds, which was %s "),
    OtpMobileResp("Please enter the OTP that was sent to your registered phone number in 100 Seconds.\n Mobile number starts with %s."),
    UserNotExist("Provided username is not exists in Let's Code Platform.\n [Register](%s/register)"),
    MobileNotExist("Hello, Coder! It seems you haven't provided your phone number for the platform.\n Please add your phone number in the Let's Code profile section and return here. I look forward to seeing you again.\n [Add Phone number](https://www.linkedin.com/in/sasidhar-maginam-790369203/)"),
    AtProblemTypes("A,B,C,D,E,F,G"),
    LcDifficultyTypes("Easy,Medium,Hard"),
    AllRandomResp("Here is your random question !\n\n"),
    SolvedRandomResp("Here is your solved question !\n\n"),
    LcRandomResp("Here is your leetcode random question !\n\n"),
    CfRandomResp("Here is your codeforces random question !\n\n"),
    AtRandomResp("Here is your atcoder random question !\n\n"),
    AtprobRandomResp("Select the problem type!\n\n"),
    LcprobRandomResp("Select the difficluty of a problem!\n\n"),
    CfprobRandomResp("Select the rating of a problem!\n\n"),
    AdminResp("Hi XX! 👋, \nTo Verify You can you select one of the option!"),
    MainNameResp("Hi XX! 👋, \nWelcome to Let's Code! 🚀 \nHere are some commands to get started:"),
    LOGIN_RESP( "Hi Coder! 👋\n\nCan I know your username to log in?\n Please reply with @username\n eg: @sasidharmsr."),
    MAIN_RESP("Welcome to Let's Code! 🚀 \nHere are some commands to get started:"),

    MailOtpMessage("Welcome to Let's Code! 🚀 "+
                      "\n" +
                      "Dear [xx],\n" +
                      "\n" +
                      "We're excited to welcome you to Letscode, where you can recieve for daily coding challenges!\n" +
                      "\n" +
                      "Here is your One-Time Password (OTP) to get started:\n" +
                      "OTP: [otp] \n" +
                      "\n" +
                      "Please use this OTP to connect with the LetsCode Bot. \n"+
                      "\n" +
                      "Happy coding!\n" +
                      "\n" +
                      "Best regards,\n" +
                      "Msr"),


    ForgotPassword("<p>Hi %s,</p>"
    + "<p>We received a request to reset your password on the LetsCode platform.</p>"
    + "<p>Please click the button below to reset your password:</p>"
    + "<a href=%s style='display: inline-block; padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none;'>Reset Password</a>"
    + "<p>If you didn't request this password reset, you can safely ignore this email.</p>"
    + "<p>Thank you,</p>"
    + "<p>LetsCode Platform</p>"),


    WelcomePage("<!DOCTYPE html>\n" +
        "<html>\n" +
        "<head>\n" +
        "    <style>\n" +
        "        body {\n" +
        "            font-family: Arial, Helvetica, sans-serif;\n" +
        "            background-color: #f2f2f2;\n" +
        "            margin: 0;\n" +
        "            padding: 0;\n" +
        "        }\n" +
        "        .container {\n" +
        "            max-width: 600px;\n" +
        "            margin: 0 auto;\n" +
        "            padding: 20px;\n" +
        "            background-color: #fff;\n" +
        "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
        "        }\n" +
        "        h1 {\n" +
        "            color: #333;\n" +
        "        }\n" +
        "        p {\n" +
        "            color: #555;\n" +
        "        }\n" +
        "        .list-item {\n" +
        "            margin-bottom: 10px;\n" +
        "        }\n" +
        "        .list-item i {\n" +
        "            margin-right: 5px;\n" +
        "        }\n" +
        "    </style>\n" +
        "</head>\n" +
        "<body>\n" +
        "    <div class=\"container\">\n" +
        "        <h1>Welcome to Let's Code Platform</h1>\n" +
        "        <p>Dear [XXX],</p>\n" +
        "        <p>We are thrilled to welcome you to the Let's Code Platform, your gateway to a world of coding challenges and collaborative learning.</p>\n" +
        "        <p>At Let's Code, we've built an interactive coding website with a passion for coding, problem-solving, and continuous improvement. Our platform brings together a community of like-minded individuals who are dedicated to enhancing their coding skills, seeking answers to their doubts, exploring coding problems, and reviewing their past submissions.</p>\n" +
        "        <h2>Here's what you can expect from Let's Code:</h2>\n" +
        "        <ul>\n" +
        "            <li class=\"list-item\"><i class=\"fas fa-code\"></i> Wide Range of Challenges: We offer daily coding challenges sourced from renowned programming platforms such as Codeforces, AtCoder, and LeetCode. These challenges are designed to stimulate your problem-solving abilities and keep you engaged.</li>\n" +
        "            <li class=\"list-item\"><i class=\"fas fa-comments\"></i> Engaging User Interactions: Connect with fellow coders through our user-friendly interface. You can post questions, share insights, like, comment, and follow other users to create a vibrant and supportive community.</li>\n" +
        "            <li class=\"list-item\"><i class=\"fas fa-user-friends\"></i> Codeforces Integration: Explore our unique Codeforces version of the Kenkoooo website. Gain insights into your followers' solved problems and solutions, and watch your progress grow.</li>\n" +
        "        </ul>\n" +
        "        <p>We are excited to have you on board and look forward to witnessing your coding journey unfold. Whether you're a seasoned coder or just starting out, Let's Code offers a welcoming environment where you can learn, grow, and collaborate with others who share your passion.</p>\n" +
        "        <p>Feel free to start exploring our platform, solving challenges, and engaging with the community. If you have any questions or need assistance, please don't hesitate to reach out to our support team.</p>\n" +
        "        <p>Happy coding!</p>\n" +
        "        <p>Best regards,</p>\n" +
        "        <p>MSR</p>\n" +
        "        <p>P.S. Stay tuned for your daily coding challenges delivered right to your inbox by our automated assistant, LetsCode Bot on Telegram!</p>\n" +
        "    </div>\n" +
        "</body>\n" +
        "</html>");


    private final String message;

    Telegrammessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
