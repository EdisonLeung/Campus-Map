let resumeData = {
  imagebaseurl: "https://rbhatia46.github.io/",
  name: "Edison Leung | Software Developer",
  linkedinId: "Your LinkedIn Id",
  roleDescription:
    "I love exploring and creating new technologies. I'm always trying to find something funny in any situation.",
  socialLinks: [
    {
      name: "linkedin",
      url: "https://www.linkedin.com/in/edleung03/",
      className: "fa fa-linkedin",
    },
    {
      name: "github",
      url: "https://github.com/EdisonLeung/",
      className: "fa fa-github",
    },
    {
      name: "resume",
      url: "https://drive.google.com/file/d/1qwD42YlEOCNcUD_RuuOGs7OXyMwjLYPe/view?usp=sharing",
      className: "fa fa-file",
    },
  ],
  aboutme:
    "Experienced in programming with various programming languages. Hands on experience with IOS and Android mobile app development. Seeking opportunities to pursue my passion in computer science and learn more about its applications",
  email: "edleung03@gmail.com",
  number: "+1(206)681-7124",
  education: [
    {
      UniversityName: "University of Washington",
      date: "09/2021 - Current",
      info: [
        "Pursuing B.S. in Computer Science from Paul G. Allen Center for Computer Science and Engineering",
        "Major GPA: 3.87",
      ],
      courses: [
        "Software Design and Implementation",
        "Hardware Software Interface",
        "Systems and Software Tools",
        "Foundations of Computing",
        "Data Structures and Parallelism",
        "Calculus Series and Linear Algebra",
      ],
      clubs:
        "Advanced Robotics (ARUW), Association for Computing Machinery (ACM), Asian American InterVarsity (AAIV)",
    },
  ],
  work: [
    {
      CompanyName: "CSE Course TA / University of Washington",
      logo: "images/PaulAllenLogo.png",
      date: "09/2022 - Current",
      Achievements: [
        "Computer Science Course TA for Software Design and Implementation and Systems and Software Tools courses",
        "Improved grading efficiency by writing auto-graders in python to quickly and accurately grade student’s code submissions and automatically upload scores to UW’s Learning Management Systems",
        "Held office hours and led additional optional lecture sessions to help students better understand lecture material",
      ],
    },
    {
      CompanyName: "Solala / Software Engineer",
      date: "06/2022 - 09/2022",
      logo: "images/Solala.jpg",
      Achievements: [
        "Full-Stack developer for website, IOS, and Android mobile app. Used expo for easy mobile app integration",
        "Worked on client-server development and created database responsible for storing and retrieving user information using firebase",
        "Used React Native and JavaScript for common UI and code reuse between web, IOS, and Android platforms",
        "Programmed UI components such as a task manager, calendar, reminders tab, and navigation manager",
        "Designed and created user friendly interface using CSS and React Native components",
        "Optimized sorting algorithm to organize user data efficiently",
      ],
    },
    {
      CompanyName: "Coding Instructor / ID Tech",
      date: "03/2021-09/2021, 06/2022-08/20220",
      logo: "images/IDTech.jpg",
      Achievements: [
        "Taught object-oriented design and how to apply it in Java and C#",
        "Taught game design principles through 3D game creation using Unity and API usage and integration through Minecraft modding",
        "Developed game components for client-server interface",
        "Programmed using Lua to develop and publish online games on Roblox",
        "Developed and improved programming curriculum to better focus on understanding why code works rather than how it works",
      ],
    },
  ],
  skillsDescription: "",
  skills: [
    {
      skillname: "Java",
    },
    {
      skillname: "JavaScript",
    },
    {
      skillname: "HTML5/CSS",
    },
    {
      skillname: "Python",
    },
    {
      skillname: "PostgreSQL",
    },
    {
      skillname: "C/C++",
    },
    {
      skillname: "C#",
    },
    {
      skillname: "Git",
    },
    {
      skillname: "TypeScript",
    },
    {
      skillname: "React/React Native",
    },
    {
      skillname: "Tailwind",
    },
    {
      skillname: "Node.js",
    },
    {
      skillname: "Linux",
    },
    {
      skillname: "Firebase",
    },
    {
      skillname: "Mobile App and Client-Server Development",
    },
    {
      skillname: "Database Management",
    },
    {
      skillname: "Object Oriented Design",
    },
    {
      skillname: "Full-Stack Development",
    },
  ],
  portfolio: [
    {
      name: "UW Campus Map",
      description: [
        "An application for finding the shortest path from one building to another at the University of Washington",
      ],
      imgurl: "images/portfolio/CampusMap.jpg",
      skills: [
        "React",
        "Typescript",
        "Node.js",
        "Spark Framework",
        "Google API",
        "JUnit Testing",
        "Java",
      ],
      link: "/CampusMap",
      gitLink: "https://github.com/EdisonLeung/Campus-Map",
      textLinks: [{ link: "/CampusMap", text: "View Project" }],
    },
    {
      name: "Imagetagger for Advanced Robotics at UW",
      description: [
        "An Image Annotation Tool to effectively process over 30k+ images to feed into Machine Learning Model",
      ],
      imgurl: "images/portfolio/Imagetagger.jpg",
      skills: ["React", "Tailwind", "PostgreSQL", "Prisma", "Git"],
      link: "",
      textLinks: [],
    },
    {
      name: "Robotics @ Cedar Park",
      description: [
        "Designed a robot’s functions per FIRST Robotics Competition’s (FRC) yearly performance and budget requirements, working closely with mentors from Boeing and Microsoft",
        "From 2020-2021 I was the software lead for the shooting division which was responsible for determining the ideal trajectory to hit a target given distance data.",
        "Some of my other responsibilities included: Building a SLAM Map to enable robot to know location on field, Using SLAM Map to develop an algorithm to assist robot driver to properly align robot with targets, and Managing Electrical and Software compatability"
      ],
      imgurl: "images/portfolio/CPR_Robot.jpg",
      skills: ["Java", "SLAM Mapping", "WPILIB", "Git", "Vision Tracking", "Cubic Interpolation", "Matlab", "Hardware Software Interface"],
      link: "",
      textLinks: [{ link: "https://www.cpr3663.com/", text: "Team Website" },
      //  {link: "", text: "View Project"}
      ],
    },
  ],
  noteworthy_projects: [
    {
      name: "Dynamic Memory Allocator",
      description:
        "Memory allocator that increases space utilization and decrease throughput using matrix transpose algorithms to optimize cache usage and reduce cache misses",
      link: "https://github.com/EdisonLeung/Dynamic-Memory-Allocator",
      skills: ["C", "blocking", "Memory Allocation in C"],
    },
  ],
};

export default resumeData;
