from datetime import datetime
import os, textwrap, re

# Define data
categories = [
    (1, 'IT', 'IT'),
    (2, 'LANGUAGES', 'Languages')
]

directions = [
    (1, 'ENGLISH', 'English', 2),
    (2, 'UKRAINIAN', 'Ukrainian', 2),
    (3, 'POLISH', 'Polish', 2),
    (4, 'FULL_STACK', 'Full Stack', 1),
    (5, 'FRONTEND', 'Frontend', 1),
    (6, 'SALES_MANAGER', 'Sales Manager', 1),
    (7, 'QA', 'QA', 1),
    (8, 'BUSINESS_ANALYSIS', 'Business Analysis', 1),
    (9, 'GAME_DEV', 'Game Development', 1),
    (10, 'CYBER_SECURITY', 'Cyber Security', 1),
    (11, 'DEVOPS', 'DevOps', 1),
    (12, 'DATA_SCIENCE', 'Data Science', 1),
    (13, 'MOBILE', 'Mobile Development', 1),
    (14, 'BACKEND', 'Backend', 1),
    (15, 'DESIGN', 'Design', 1)
]

topics = [
 (1,'A1'),(2,'A2'),(3,'B1'),(4,'B2'),(5,'IELTS'),(6,'Business'),(7,'Travel'),
 (8,'Java'),(9,'Spring'),(10,'React'),(11,'Docker'),(12,'Kubernetes'),(13,'SQL'),
 (14,'HTML'),(15,'CSS'),(16,'JavaScript'),(17,'Node.js'),(18,'Git'),(19,'HTTP'),
 (20,'TypeScript'),(21,'Vue.js'),(22,'Angular'),(23,'C#'),(24,'Unity'),(25,'Unreal'),
 (26,'C++'),(27,'Python'),(28,'Machine Learning'),(29,'Data Analysis'),(30,'TensorFlow'),
 (31,'PyTorch'),(32,'Statistics'),(33,'Hadoop'),(34,'Spark'),(35,'Kotlin'),(36,'Swift'),
 (37,'Dart'),(38,'Flutter'),(39,'React Native'),(40,'Go'),(41,'Rust'),(42,'Microservices'),
 (43,'AWS'),(44,'Azure'),(45,'GCP'),(46,'Terraform'),(47,'Jenkins'),(48,'Scrum'),(49,'Agile'),
 (50,'UX'),(51,'Figma'),(52,'Photoshop'),(53,'Illustrator'),(54,'Penetration Testing'),
 (55,'Security+'),(56,'Networking'),(57,'SRE'),(58,'Selenium'),(59,'Cypress'),
 (60,'Appium'),(61,'Android'),(62,'iOS'),(63,'Design Thinking')
]

# Direction to topic ids mapping
direction_topics_map = {
    1:[1,2,3,4,5,6,7],
    2:[1,2,3,4,6,7],
    3:[1,2,3,4,6,7],
    4:[14,15,16,10,17,18,11,12,13,42],
    5:[14,15,16,10,21,22,20,18],
    6:[6,48,49],
    7:[58,59,60,8,27],
    8:[6,48,49,13,29],
    9:[23,24,25,26,18,63],
    10:[54,55,56,43],
    11:[11,12,46,47,43,44,45,18,57],
    12:[27,28,29,32,30,31,13,33,34],
    13:[35,36,37,38,39,8,61,62],
    14:[8,9,17,27,13,40,41,42,11,12],
    15:[50,51,52,53,14,15,63]
}

# Courses data: list of dicts
courses = []
cid = 1
def add_course(title, desc, url, price, directions_codes, topics_titles):
    global cid
    courses.append({
        'id': cid,
        'title': title,
        'description': desc,
        'url': url,
        'price': price,
        'directions': directions_codes,
        'topics': topics_titles
    })
    cid += 1

# helper to map topic title to id
topic_title_to_id = {t[1]:t[0] for t in topics}
direction_code_to_id = {d[1]:d[0] for d in directions}

# English courses
add_course('English for Career Development','Improve job‑search English skills','https://www.coursera.org/learn/careerdevelopment',0.00,['ENGLISH'],['Business','B2'])
add_course('Business English Communication Skills','Complete business English specialization','https://www.coursera.org/specializations/business-english',0.00,['ENGLISH'],['Business','B2'])
add_course('IELTS Academic Test Preparation','Official IELTS prep','https://www.edx.org/course/ielts-academic-test-preparation',0.00,['ENGLISH'],['IELTS','B2'])
add_course('English Grammar Launch','Interactive A2 grammar course','https://www.udemy.com/course/english-grammar-launch/',19.99,['ENGLISH'],['A2'])
add_course('English for Travel','Survival English for travellers','https://www.futurelearn.com/courses/exploring-english-travel',0.00,['ENGLISH'],['Travel','A2'])
add_course('Speak English Professionally','B2 speaking practice','https://www.coursera.org/learn/speak-english-professionally',0.00,['ENGLISH'],['B2','Business'])
add_course('Cambridge English B2 First Preparation','Prep for Cambridge B2 First','https://www.futurelearn.com/courses/fce-test-preparation',59.00,['ENGLISH'],['B2'])

# Ukrainian
add_course('Ukrainian Language for Beginners','Starter A1 course','https://www.udemy.com/course/ukrainian-language-for-beginners/',19.99,['UKRAINIAN'],['A1'])
add_course('Ukrainian Language Elementary A2','Elementary A2 course','https://www.udemy.com/course/learn-ukrainian-a2/',19.99,['UKRAINIAN'],['A2'])
add_course('Ukrainian for Foreigners','Comprehensive basic Ukrainian','https://courses.prometheus.org.ua',0.00,['UKRAINIAN'],['A1','A2'])
add_course('Ukrainian Pronunciation and Listening','Pronunciation drills','https://www.udemy.com/course/ukrainian-pronunciation/',19.99,['UKRAINIAN'],['B1'])
add_course('Business Ukrainian','Language for business contexts','https://example.com/business-ukrainian',0.00,['UKRAINIAN'],['Business'])
add_course('Ukrainian B1 Conversation','Intermediate speaking','https://example.com/ukrainian-b1',0.00,['UKRAINIAN'],['B1'])
add_course('Ukrainian B2 Advanced','Advanced grammar and style','https://example.com/ukrainian-b2',0.00,['UKRAINIAN'],['B2'])

# Polish
add_course('Polish for Beginners','Starter A1 Polish','https://www.udemy.com/course/polish-for-beginners/',19.99,['POLISH'],['A1'])
add_course('Polish Language A2','Elementary Polish','https://www.udemy.com/course/polish-a2/',19.99,['POLISH'],['A2'])
add_course('Polish Intermediate B1','Intermediate grammar','https://example.com/polish-b1',0.00,['POLISH'],['B1'])
add_course('Polish Advanced B2','Advanced Polish','https://example.com/polish-b2',0.00,['POLISH'],['B2'])
add_course('Business Polish','Corporate communication','https://example.com/business-polish',0.00,['POLISH'],['Business'])
add_course('Polish Pronunciation','Pronunciation and phonetics','https://example.com/polish-pronunciation',0.00,['POLISH'],['B1'])
add_course('Polish for Travel','Travel phrases','https://example.com/polish-travel',0.00,['POLISH'],['Travel'])

# Full Stack
add_course('Meta Full Stack Developer','Meta professional certificate','https://www.coursera.org/professional-certificates/meta-back-end-developer',0.00,['FULL_STACK','BACKEND','FRONTEND'],['Java','React','Docker'])
add_course('Full-Stack Web Development with React','HKUST specialization','https://www.coursera.org/specializations/full-stack-react',0.00,['FULL_STACK','FRONTEND'],['React','JavaScript','Docker'])
add_course('IBM Full Stack Cloud Developer','IBM professional cert','https://www.coursera.org/professional-certificates/ibm-full-stack',0.00,['FULL_STACK'],['Docker','Kubernetes'])
add_course('Complete Web Dev Bootcamp 2025','Full stack JS bootcamp','https://www.udemy.com/course/the-complete-web-development-bootcamp/',19.99,['FULL_STACK'],['JavaScript','Node.js','Git'])
add_course('The Odin Project Full Stack','Free open curriculum','https://www.theodinproject.com',0.00,['FULL_STACK'],['HTML','CSS','JavaScript'])
add_course('Codecademy Full-Stack Engineer','Career path','https://www.codecademy.com/learn/paths/full-stack-engineer-career-path',0.00,['FULL_STACK'],['JavaScript','Git'])
add_course('Full Stack Open','University of Helsinki online','https://fullstackopen.com/en/',0.00,['FULL_STACK'],['JavaScript','React','Node.js'])

# Frontend
add_course('React - The Complete Guide','React 18 & Next.js','https://www.udemy.com/course/react-the-complete-guide-incl-redux/',19.99,['FRONTEND'],['React'])
add_course('Modern JavaScript From The Beginning','Project-based JS','https://www.udemy.com/course/modern-javascript-from-the-beginning/',19.99,['FRONTEND'],['JavaScript'])
add_course('Responsive Web Design','Certification path','https://www.freecodecamp.org/learn/',0.00,['FRONTEND'],['HTML','CSS'])
add_course('Advanced CSS and Sass','Flexbox & Grid','https://www.udemy.com/course/advanced-css-and-sass/',19.99,['FRONTEND'],['CSS'])
add_course('Front-End Web UI','Bootstrap & JS','https://www.coursera.org/learn/front-end-react',0.00,['FRONTEND'],['React','HTML'])
add_course('Vue JS Complete Guide','Vue 3 deep dive','https://www.udemy.com/course/vuejs-2-the-complete-guide/',19.99,['FRONTEND'],['Vue.js'])
add_course('Angular - The Complete Guide','Angular 17','https://www.udemy.com/course/the-complete-guide-to-angular-2/',19.99,['FRONTEND'],['Angular'])

# Sales Manager
for title, url in [
    ('Sales Training: Building Your Sales Career','https://www.coursera.org/learn/sales-training'),
    ('HubSpot Sales Enablement','https://academy.hubspot.com/courses/sales-enablement'),
    ('Sales Strategy','https://www.linkedin.com/learning/sales-strategy'),
    ('Salesforce Sales Representative','https://trailhead.salesforce.com/en/credentials/salescloudconsultant'),
    ('B2B Sales Masterclass','https://www.udemy.com/course/b2b-sales-masterclass/'),
    ('Negotiation Fundamentals','https://www.coursera.org/learn/negotiation-fundamentals'),
    ('Inside Sales Foundations','https://www.linkedin.com/learning/inside-sales-foundations')
]:
    add_course(title,'Sales skills course',url,0.00,['SALES_MANAGER'],['Business'])

# QA
qa_courses = [
    ('Software Testing and Automation','https://www.coursera.org/specializations/software-testing-automation',['Selenium','Docker']),
    ('ISTQB Foundation Exam Preparation','https://www.udemy.com/course/istqb-foundation/',['Selenium']),
    ('Automated Software Testing with Python','https://www.udemy.com/course/automated-software-testing-with-python/',['Python','Selenium']),
    ('Selenium WebDriver with Java','https://www.udemy.com/course/selenium-real-time-examplesinterview-questions/',['Java','Selenium']),
    ('QA Engineer Bootcamp','https://testpro.io',['Selenium']),
    ('Mobile Testing with Appium','https://www.udemy.com/course/appium-selenium-for-mobile-automation-testing/',['Appium']),
    ('Cypress End-to-End Testing','https://www.udemy.com/course/cypress-qa-automation/',['Cypress'])
]
for title,url,tops in qa_courses:
    add_course(title,'QA/testing course',url,19.99,['QA'],tops)

# Business Analysis
ba_courses = [
    ('Google Business Intelligence','https://www.coursera.org/professional-certificates/google-business-intelligence',['Business','SQL','Data Analysis']),
    ('Introduction to Business Analysis','https://www.coursera.org/learn/business-analysis',['Business']),
    ('Business Analysis Fundamentals','https://www.udemy.com/course/business-analysis-fundamentals/',['Business']),
    ('Agile Business Analysis','https://www.linkedin.com/learning/agile-business-analysis',['Agile','Scrum']),
    ('BABOK Certification Preparation','https://www.udemy.com/course/babok-v3/',['Business']),
    ('Data Analytics for Business','https://www.coursera.org/learn/wharton-data-analytics',['Data Analysis']),
    ('Requirements Engineering','https://www.edx.org/course/requirements-engineering',['Business'])
]
for title,url,tops in ba_courses:
    add_course(title,'Business analysis course',url,0.00,['BUSINESS_ANALYSIS'],tops)

# Game Dev
gd_courses = [
    ('Game Design and Development','https://www.coursera.org/specializations/game-development',['Unity']),
    ('Unreal Engine C++ Developer','https://www.udemy.com/course/unrealcourse/',['C++','Unreal']),
    ('Unity 2D Game Development','https://www.coursera.org/projects/unity-2d-platformer',['Unity']),
    ('Complete C# Unity Game Developer','https://www.udemy.com/course/unitycourse2/',['C#','Unity']),
    ('Introduction to Game Development','https://www.edx.org/professional-certificate/michiganx-game-development',['C#','Unity']),
    ('Godot Engine Beginner','https://www.udemy.com/course/godot/',['Unity']),
    ('Game Programming Patterns','https://gameprogrammingpatterns.com/',['C#'])
]
for title,url,tops in gd_courses:
    add_course(title,'Game development course',url,0.00,['GAME_DEV'],tops)

# Cyber Security
cy_courses = [
    ('Google Cybersecurity Professional','https://www.coursera.org/professional-certificates/google-cybersecurity',['Security+','Networking']),
    ('Introduction to Cyber Security','https://www.udemy.com/course/the-complete-cyber-security-course-hacker-exposed/',['Penetration Testing']),
    ('CompTIA Security+ Complete','https://www.udemy.com/course/comptia-security-certification/',['Security+']),
    ('Penetration Testing and Ethical Hacking','https://www.coursera.org/specializations/penetration-testing',['Penetration Testing']),
    ('Cybersecurity Fundamentals','https://www.edx.org/course/cybersecurity-fundamentals',['Security+']),
    ('Network Security Basics','https://www.linkedin.com/learning/network-security-basics',['Networking']),
    ('Incident Response & Handling','https://www.udemy.com/course/incident-response/',['Penetration Testing'])
]
for title,url,tops in cy_courses:
    add_course(title,'Cyber security course',url,0.00,['CYBER_SECURITY'],tops)

# DevOps
devops_courses = [
    ('DevOps on AWS','https://www.coursera.org/specializations/aws-devops',['AWS','Docker']),
    ('CI/CD Pipelines with Jenkins','https://www.coursera.org/learn/jenkins-continuous-integration',['Jenkins']),
    ('Docker & Kubernetes: The Complete Guide','https://www.udemy.com/course/docker-and-kubernetes-the-complete-guide/',['Docker','Kubernetes']),
    ('Terraform on Azure','https://www.udemy.com/course/terraform-on-azure/',['Terraform','Azure']),
    ('Google Cloud DevOps','https://www.coursera.org/specializations/devops-cicd',['GCP','Docker']),
    ('Site Reliability Engineering','https://www.udemy.com/course/site-reliability-engineering/',['SRE']),
    ('Kubernetes for Developers','https://www.coursera.org/learn/google-kubernetes-engine',['Kubernetes'])
]
for title,url,tops in devops_courses:
    add_course(title,'DevOps course',url,19.99,['DEVOPS'],tops)

# Data Science
ds_courses = [
    ('IBM Data Science Professional','https://www.coursera.org/professional-certificates/ibm-data-science',['Python','Data Analysis']),
    ('Machine Learning','https://www.coursera.org/learn/machine-learning',['Machine Learning']),
    ('Data Science MicroMasters','https://www.edx.org/micromasters/mitx-statistics-and-data-science',['Statistics','Data Analysis']),
    ('Python for Data Science & ML','https://www.udemy.com/course/python-for-data-science-and-machine-learning-bootcamp/',['Python','Machine Learning']),
    ('Data Analysis with Python','https://www.freecodecamp.org/learn/data-analysis-with-python/',['Python','Data Analysis']),
    ('Big Data with Hadoop & Spark','https://www.udemy.com/course/the-ultimate-hands-on-hadoop/',['Hadoop','Spark']),
    ('TensorFlow Developer Certificate','https://www.coursera.org/professional-certificates/tensorflow-in-practice',['TensorFlow'])
]
for title,url,tops in ds_courses:
    add_course(title,'Data science course',url,0.00,['DATA_SCIENCE'],tops)

# Mobile
mobile_courses = [
    ('Android Kotlin Developer','https://www.udacity.com/course/android-developer-kotlin--nd940',['Kotlin','Android']),
    ('iOS App Development with Swift','https://www.coursera.org/specializations/ios-app-development',['Swift','iOS']),
    ('Flutter & Dart - The Complete Guide','https://www.udemy.com/course/learn-flutter-dart-to-build-ios-android-apps/',['Dart','Flutter']),
    ('React Native - Mobile Apps','https://www.udemy.com/course/the-complete-react-native-and-redux-course/',['React Native','React']),
    ('Java for Android','https://www.coursera.org/specializations/java-android',['Java','Android']),
    ('Advanced Android with Jetpack Compose','https://www.udemy.com/course/jetpack-compose-android-development-primer/',['Kotlin','Android']),
    ('Mobile Development with Xamarin','https://www.linkedin.com/learning/mobile-development-with-xamarin-forms',['C#','Android'])
]
for title,url,tops in mobile_courses:
    add_course(title,'Mobile dev course',url,19.99,['MOBILE'],tops)

# Backend
backend_courses = [
    ('Java Programming and Software Engineering Fundamentals','https://www.coursera.org/specializations/java-programming',['Java','SQL']),
    ('Spring Framework - Beginner to Guru','https://www.udemy.com/course/spring-framework-5-beginner-to-guru/',['Spring','Java']),
    ('Node.js Backend Developer Bootcamp','https://www.udemy.com/course/the-complete-nodejs-developer-course-2/',['Node.js','JavaScript']),
    ('Python Django Web Development','https://www.udemy.com/course/python-and-django-full-stack-web-developer-bootcamp/',['Python','SQL']),
    ('.NET Core Web API Development','https://www.udemy.com/course/rest-api-with-asp-net-core-web-api/',['C#']),
    ('Go Backend Engineering','https://www.udemy.com/course/go-the-complete-developers-guide/',['Go']),
    ('Rust Web Services with Actix','https://www.udemy.com/course/rust-webservices-with-actix/',['Rust'])
]
for title,url,tops in backend_courses:
    add_course(title,'Backend dev course',url,19.99,['BACKEND'],tops)

# Design
design_courses = [
    ('UI / UX Design Specialization','https://www.coursera.org/specializations/ui-ux-design',['UX','Figma']),
    ('Graphic Design Basics','https://www.edx.org/course/graphic-design',['UX']),
    ('Adobe Photoshop Masterclass','https://www.udemy.com/course/photoshop-mastery/',['Photoshop']),
    ('Interaction Design','https://www.interaction-design.org/courses',['UX']),
    ('Figma for UI Design','https://www.udemy.com/course/figma-ux-ui-app-design/',['Figma']),
    ('Modern Web Design','https://www.linkedin.com/learning/building-websites-in-2025',['HTML','CSS']),
    ('Design Thinking for Innovation','https://www.coursera.org/learn/uva-darden-design-thinking-innovation',['Design Thinking'])
]
for title,url,tops in design_courses:
    add_course(title,'Design course',url,0.00,['DESIGN'],tops)

# Build SQL
def esc(val):
    return val.replace("'", "''")

sql_lines = []

# Categories
sql_lines.append("-- Categories")
sql_lines.append("INSERT INTO categories (id, code, title) VALUES")
sql_lines.append(",\n".join(f"  ({cid},'{code}','{title}')" for cid,code,title in categories) + ";")

# Directions
sql_lines.append("\n-- Directions")
sql_lines.append("INSERT INTO directions (id, code, title, category_id) VALUES")
sql_lines.append(",\n".join(f"  ({did},'{code}','{title}',{cat})" for did,code,title,cat in directions) + ";")

# Topics
sql_lines.append("\n-- Topics")
sql_lines.append("INSERT INTO topics (id, title) VALUES")
sql_lines.append(",\n".join(f"  ({tid},'{title}')" for tid,title in topics) + ";")

# Direction topics
sql_lines.append("\n-- Direction ↔ Topic")
sql_dir_top = []
for did, tlist in direction_topics_map.items():
    for tid in tlist:
        sql_dir_top.append(f"  ({did},{tid})")
sql_lines.append("INSERT INTO direction_topics (direction_id, topic_id) VALUES")
sql_lines.append(",\n".join(sql_dir_top) + ";")

# Courses
sql_lines.append("\n-- Courses")
sql_lines.append("INSERT INTO courses (id, title, description, preview_url, origin_course_url, price, created_at, like_count, dislike_count, comment_count) VALUES")
course_values = []
for c in courses:
    course_values.append(f"  ({c['id']},'{esc(c['title'])}','{esc(c['description'])}','{esc(c['url'])}','{esc(c['url'])}',{c['price']:.2f},NOW(),0,0,0)")
sql_lines.append(",\n".join(course_values) + ";")

# Course directions
sql_lines.append("\n-- Course ↔ Direction")
cd_values = []
for c in courses:
    for dir_code in c['directions']:
        did = direction_code_to_id[dir_code]
        cd_values.append(f"  ({c['id']},{did})")
sql_lines.append("INSERT INTO course_directions (course_id, direction_id) VALUES")
sql_lines.append(",\n".join(cd_values) + ";")

# Course topics
sql_lines.append("\n-- Course ↔ Topic")
ct_values = []
for c in courses:
    for ttitle in c['topics']:
        tid = topic_title_to_id[ttitle]
        ct_values.append(f"  ({c['id']},{tid})")
sql_lines.append("INSERT INTO course_topics (course_id, topic_id) VALUES")
sql_lines.append(",\n".join(ct_values) + ";")

sql_content = "\n".join(sql_lines)

# Write to file
file_path = '/mnt/data/V1__init_real_courses.sql'
with open(file_path, 'w', encoding='utf-8') as f:
    f.write(sql_content)

file_path
