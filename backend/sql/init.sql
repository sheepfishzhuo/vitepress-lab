-- 创建数据库
CREATE DATABASE IF NOT EXISTS lab_website DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE lab_website;

-- 教师表
CREATE TABLE IF NOT EXISTS faculty (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    title VARCHAR(50),
    role VARCHAR(100),
    avatar VARCHAR(500),
    email VARCHAR(100),
    phone VARCHAR(50),
    office VARCHAR(100),
    research_interests TEXT,
    education TEXT,
    biography TEXT,
    google_scholar VARCHAR(500),
    research_gate VARCHAR(500),
    orcid VARCHAR(100),
    personal VARCHAR(500),
    github VARCHAR(200),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 学生表
CREATE TABLE IF NOT EXISTS student (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    avatar VARCHAR(500),
    degree ENUM('bachelor', 'master', 'phd'),
    advisor VARCHAR(100),
    year INT,
    research_interests TEXT,
    projects TEXT,
    awards TEXT,
    email VARCHAR(100),
    biography TEXT,
    status ENUM('enrolled', 'graduated'),
    graduation_year INT,
    current_position VARCHAR(200),
    linkedin VARCHAR(200),
    github VARCHAR(200),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 论文表
CREATE TABLE IF NOT EXISTS paper (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(500) NOT NULL,
    authors TEXT,
    venue VARCHAR(200),
    year INT,
    type ENUM('journal', 'conference', 'workshop', 'preprint'),
    doi VARCHAR(200),
    abstract_text TEXT,
    keywords TEXT,
    pdf VARCHAR(500),
    code VARCHAR(500),
    citations INT,
    impact_factor DOUBLE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 项目表
CREATE TABLE IF NOT EXISTS project (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(300) NOT NULL,
    principal_investigator VARCHAR(100),
    members TEXT,
    funding VARCHAR(200),
    amount VARCHAR(100),
    start_date VARCHAR(50),
    end_date VARCHAR(50),
    status ENUM('ongoing', 'completed'),
    description TEXT,
    outcomes TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 新闻表
CREATE TABLE IF NOT EXISTS news (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(300) NOT NULL,
    date VARCHAR(50),
    category ENUM('research', 'award', 'event', 'announcement'),
    summary TEXT,
    content LONGTEXT,
    image VARCHAR(500),
    author VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 竞赛表
CREATE TABLE IF NOT EXISTS competition (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(300) NOT NULL,
    level ENUM('national', 'provincial', 'international'),
    award VARCHAR(100),
    year INT,
    participants TEXT,
    advisors TEXT,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 会议表
CREATE TABLE IF NOT EXISTS conference (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(300) NOT NULL,
    location VARCHAR(200),
    date VARCHAR(50),
    type ENUM('organize', 'present', 'attend'),
    participants TEXT,
    description TEXT,
    link VARCHAR(500),
    image VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 专利表
CREATE TABLE IF NOT EXISTS patent (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(300) NOT NULL,
    type ENUM('invention', 'utility', 'software'),
    inventors TEXT,
    patent_no VARCHAR(100),
    status ENUM('pending', 'granted'),
    date VARCHAR(50),
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入示例数据

-- 教师数据
INSERT INTO faculty (name, title, role, email, research_interests) VALUES
('张伟', '教授', '实验室主任', 'zhangwei@university.edu.cn', '人工智能,机器学习,计算机视觉'),
('李明', '副教授', NULL, 'liming@university.edu.cn', '深度学习,自然语言处理,知识图谱'),
('王芳', '副教授', NULL, 'wangfang@university.edu.cn', '多模态学习,计算机视觉,深度学习');

-- 学生数据
INSERT INTO student (name, degree, advisor, year, research_interests, status) VALUES
('陈小明', 'phd', '张伟', 2021, '深度学习,图像识别', 'enrolled'),
('刘洋', 'master', '李明', 2022, '自然语言处理,大语言模型', 'enrolled'),
('赵静', 'master', '王芳', 2022, '多模态学习,视觉-语言模型', 'enrolled'),
('孙浩', 'bachelor', '张伟', 2023, '机器学习基础', 'enrolled'),
('王强', 'phd', '王芳', 2020, '计算机视觉,目标检测', 'enrolled'),
('黄林', 'master', '张伟', 2020, '深度学习', 'graduated');

-- 论文数据
INSERT INTO paper (title, authors, venue, year, type, doi, citations, impact_factor) VALUES
('Deep Learning for Image Recognition: A Survey', '张伟,陈小明,王芳', 'IEEE TPAMI', 2024, 'journal', '10.1109/TPAMI.2024.1234567', 156, 24.314),
('Multi-modal Learning with Vision-Language Models', '王芳,赵静', 'CVPR 2024', 2024, 'conference', '10.1109/CVPR.2024.00123', 89, NULL),
('Efficient Training of Large Language Models', '李明,刘洋', 'ACL 2024', 2024, 'conference', '10.18653/v1/2024.acl-main.123', 67, NULL),
('Neural Architecture Search: A Comprehensive Review', '张伟,王强', 'NeurIPS 2023', 2023, 'conference', '10.48550/arXiv.2312.12345', 234, NULL);

-- 项目数据
INSERT INTO project (title, principal_investigator, funding, start_date, status, description) VALUES
('面向复杂场景的多模态智能感知关键技术研究', '张伟', '国家自然科学基金', '2023-01', 'ongoing', '研究复杂场景下的多模态感知技术'),
('大语言模型高效微调方法研究', '李明', '省部级项目', '2023-06', 'ongoing', '研究大语言模型的高效微调方法'),
('工业视觉检测系统研发', '王芳', '企业横向项目', '2022-09', 'completed', '开发工业视觉检测系统');

-- 新闻数据
INSERT INTO news (title, date, category, summary, content, author) VALUES
('实验室张伟教授入选2024年度"长江学者"特聘教授', '2024-03-15', 'award', '热烈祝贺张伟教授入选长江学者特聘教授', '<p>近日，教育部公布了2024年度"长江学者奖励计划"入选名单，我实验室张伟教授成功入选特聘教授。</p><p>张伟教授长期从事人工智能与计算机视觉领域的研究工作，在深度学习、图像识别等方向取得了丰硕的研究成果。近年来，张伟教授在IEEE TPAMI、CVPR、ICCV等顶级期刊和会议发表论文50余篇，Google Scholar引用超过8000次，主持国家自然科学基金重点项目等多项国家级课题。</p><p>"长江学者奖励计划"是教育部与香港李嘉诚基金会共同启动实施的专项高层次人才计划，旨在吸引和培养造就一批具有国际领先水平的学术带头人。此次入选是对张伟教授学术成就的充分肯定，也是实验室发展的重要里程碑。</p>', '实验室办公室'),
('实验室2篇论文被CVPR 2024接收', '2024-02-28', 'research', '实验室在计算机视觉顶级会议CVPR 2024发表2篇论文', '<p>近日，计算机视觉顶级会议CVPR 2024公布了论文接收结果，我实验室共有2篇论文被接收。</p><h2>论文介绍</h2><p><strong>论文1：Multi-modal Learning with Vision-Language Models</strong></p><p>该论文研究了视觉-语言模型中的多模态学习问题，提出了一种新的跨模态对齐方法，显著提升了模型在视觉问答和图像描述任务上的性能。</p><p><strong>论文2：Efficient Vision Transformer for Dense Prediction</strong></p><p>该论文提出了一种高效的视觉Transformer架构，在保持高精度的同时大幅降低了计算复杂度，适用于实时应用场景。</p><h2>关于CVPR</h2><p>CVPR（IEEE Conference on Computer Vision and Pattern Recognition）是计算机视觉领域最顶级的国际会议之一，每年吸引全球数千名学者参与。今年的会议将于6月在西雅图举行。</p>', '王芳'),
('实验室举办人工智能前沿技术研讨会', '2024-01-20', 'event', '研讨会邀请了多位国内外知名专家学者', '<p>2024年1月20日，人工智能与大数据实验室成功举办"人工智能前沿技术研讨会"。本次研讨会采用线上线下相结合的方式，邀请了多位国内外知名专家学者做学术报告。</p><h2>会议概况</h2><p>本次研讨会围绕大语言模型、多模态学习、具身智能等前沿话题展开深入讨论。来自清华大学、北京大学、中科院自动化所等单位的专家学者分享了最新研究成果。</p><h2>精彩报告</h2><ul><li>《大语言模型的高效微调方法》- 清华大学计算机系教授</li><li>《多模态大模型的机遇与挑战》- 北京大学人工智能研究院研究员</li><li>《具身智能：从仿真到现实》- 中科院自动化所研究员</li></ul><p>研讨会吸引了校内外200余名师生参与，现场气氛热烈，讨论深入。本次研讨会的成功举办促进了学术交流，拓宽了研究视野。</p>', '李明'),
('实验室2024年春季学期招新通知', '2024-01-10', 'announcement', '欢迎对人工智能研究感兴趣的同学加入实验室', '<p>人工智能与大数据实验室现面向全校招收2024年春季学期实习生和研究生，欢迎对人工智能研究感兴趣的同学报名。</p><h2>研究方向</h2><ul><li>深度学习与神经网络架构设计</li><li>计算机视觉与图像理解</li><li>自然语言处理与大语言模型</li><li>多模态学习与跨模态理解</li></ul><h2>申请条件</h2><ul><li>计算机、电子、数学等相关专业</li><li>具有良好的编程基础（Python/C++）</li><li>对科研有热情，有较强的学习能力</li><li>每周能保证一定的科研时间</li></ul><h2>申请方式</h2><p>请将个人简历发送至邮箱：zhangwei@university.edu.cn</p><p>邮件标题请注明：姓名-专业-年级-申请方向</p><p>申请截止日期：2024年2月28日</p>', '实验室办公室');

-- 竞赛数据
INSERT INTO competition (name, level, award, year, participants) VALUES
('全国大学生数学建模竞赛', 'national', '一等奖', 2024, '刘洋,赵静,孙浩'),
('中国大学生计算机设计大赛', 'national', '二等奖', 2024, '陈小明'),
('ACM-ICPC国际大学生程序设计竞赛', 'international', '金奖', 2023, '王强,陈小明');

-- 会议数据
INSERT INTO conference (name, location, date, type, participants) VALUES
('CVPR 2024', '美国西雅图', '2024-06-17', 'present', '张伟,王芳'),
('ACL 2024', '墨西哥城', '2024-08-11', 'present', '李明,刘洋'),
('大模型时代的人工智能研究研讨会', '线上', '2024-03-01', 'organize', '张伟,李明,王芳');

-- 专利数据
INSERT INTO patent (title, type, inventors, patent_no, status, date) VALUES
('一种基于深度学习的多模态特征融合方法', 'invention', '张伟,陈小明,王芳', 'CN202410001234.5', 'granted', '2024-03-15'),
('一种面向工业场景的智能缺陷检测系统', 'invention', '王芳,赵静', 'CN202410002345.6', 'granted', '2024-02-20'),
('知识增强的语言模型预训练系统', 'software', '李明,刘洋', NULL, 'granted', '2024-01-10');

-- 配置选项表
CREATE TABLE IF NOT EXISTS config_option (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_type VARCHAR(50) NOT NULL,
    option_value VARCHAR(50) NOT NULL,
    option_label VARCHAR(100) NOT NULL,
    sort_order INT DEFAULT 0
);

-- 配置选项数据
-- 教师职称
INSERT INTO config_option (config_type, option_value, option_label, sort_order) VALUES
('faculty_title', 'professor', '教授', 1),
('faculty_title', 'associate_professor', '副教授', 2),
('faculty_title', 'lecturer', '讲师', 3),
('faculty_title', 'researcher', '研究员', 4),
('faculty_title', 'associate_researcher', '副研究员', 5);

-- 学生学位
INSERT INTO config_option (config_type, option_value, option_label, sort_order) VALUES
('student_degree', 'bachelor', '本科生', 1),
('student_degree', 'master', '硕士生', 2),
('student_degree', 'phd', '博士生', 3);

-- 学生状态
INSERT INTO config_option (config_type, option_value, option_label, sort_order) VALUES
('student_status', 'enrolled', '在读', 1),
('student_status', 'graduated', '已毕业', 2);

-- 论文类型
INSERT INTO config_option (config_type, option_value, option_label, sort_order) VALUES
('paper_type', 'journal', '期刊', 1),
('paper_type', 'conference', '会议', 2),
('paper_type', 'workshop', '研讨会', 3),
('paper_type', 'preprint', '预印本', 4);

-- 项目状态
INSERT INTO config_option (config_type, option_value, option_label, sort_order) VALUES
('project_status', 'ongoing', '进行中', 1),
('project_status', 'completed', '已完成', 2);

-- 新闻分类
INSERT INTO config_option (config_type, option_value, option_label, sort_order) VALUES
('news_category', 'research', '科研成果', 1),
('news_category', 'award', '获奖喜报', 2),
('news_category', 'event', '学术活动', 3),
('news_category', 'announcement', '通知公告', 4);

-- 竞赛级别
INSERT INTO config_option (config_type, option_value, option_label, sort_order) VALUES
('competition_level', 'international', '国际级', 1),
('competition_level', 'national', '国家级', 2),
('competition_level', 'provincial', '省级', 3);

-- 会议类型
INSERT INTO config_option (config_type, option_value, option_label, sort_order) VALUES
('conference_type', 'organize', '主办', 1),
('conference_type', 'present', '报告', 2),
('conference_type', 'attend', '参会', 3);

-- 专利类型
INSERT INTO config_option (config_type, option_value, option_label, sort_order) VALUES
('patent_type', 'invention', '发明专利', 1),
('patent_type', 'utility', '实用新型', 2),
('patent_type', 'software', '软件著作权', 3);

-- 专利状态
INSERT INTO config_option (config_type, option_value, option_label, sort_order) VALUES
('patent_status', 'pending', '申请中', 1),
('patent_status', 'granted', '已授权', 2);
