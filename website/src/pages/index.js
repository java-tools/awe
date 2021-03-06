import React from 'react';
import clsx from 'clsx';
import Layout from '@theme/Layout';
import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import useBaseUrl from '@docusaurus/useBaseUrl';
import styles from './styles.module.css';

const features = [
  {
    title: <>Easy to Use</>,
    imageUrl: 'img/undraw_just_browsing.svg',
    description: (
      <>
        AWE framework is designed from the ground up to be easily installed and
        used to build your website up and running quickly.
      </>
    ),
  },
  {
    title: <>Modern UI</>,
    imageUrl: 'img/undraw_responsive.svg',
    description: (
      <>
        All AWE web components let you design modern and responsive user interfaces using a declarative API.
        Works with <code>AngularJS</code> and <code>ReactJS</code>.
      </>
    ),
  },
  {
    title: <>Pluggable and Extensible</>,
    imageUrl: 'img/undraw_switches.svg',
    description: (
      <>
        Extend or customize all AWE features. The Spring Boot <code>starters</code> design lets you to enable the modules
        and features that you need.
      </>
    ),
  },
  {
    title: <>Connect your information</>,
    imageUrl: 'img/undraw_online_connection.svg',
    description: (
      <>
        Bind your data to web forms easily. AWE allows to connect to different data sources
        like SQL and NoSQL databases, Rest APIs, JavaBeans, etc.
      </>
    ),
  },
  {
    title: <>Customize easy</>,
    imageUrl: 'img/undraw_add_color.svg',
    description: (
      <>
        AWE has multiple preconfigured themes and multi-language support.
        You can add custom CSS according to your needs.
      </>
    ),
  },
  {
    title: <>Powered by Spring Boot with AngularJS</>,
    imageUrl: 'img/undraw_code_review.svg',
    description: (
      <>
        Uses Spring 5 and Spring Boot 2. <code>@Autowired</code> is available for AWE components and layouts.
      </>
    ),
  },
];

function Feature({imageUrl, title, description}) {
  const imgUrl = useBaseUrl(imageUrl);
  return (
    <div className={clsx('col col--4', styles.feature)}>
      {imgUrl && (
        <div className="text--center">
          <img className={styles.featureImage} src={imgUrl} alt={title}/>
        </div>
      )}
      <h3>{title}</h3>
      <p>{description}</p>
    </div>
  );
}

function Home() {
  const context = useDocusaurusContext();
  const {siteConfig = {}} = context;
  return (
    <Layout
      title={`Hello from ${siteConfig.title}`}
      description="Description will go into a meta tag in <head />">
      <div className={styles.hero}>
        <div className={styles.heroInner}>
          <h1 className={styles.heroProjectTagline}>
            <img
              alt="Awe logo"
              className={styles.heroLogo}
              src={useBaseUrl('img/logo.svg')}
            />
            Build{' '}
            <span className={styles.heroProjectKeywords}>light-weight</span>{' '} and{' '}
            <span className={styles.heroProjectKeywords}>functional</span> websites{' '}
            <span className={styles.heroProjectKeywords}>quickly</span>. Focus
            on your{' '}
            <span className={styles.heroProjectKeywords}>content.</span>
          </h1>
          <div className={styles.indexCtas}>
            <Link
              className={styles.indexCtasGetStartedButton}
              to={useBaseUrl('docs/')}>
              Start using AWE
            </Link>
            <Link
              className={clsx('margin-left--md', styles.indexLearnMoreButton)}
              to="https://www.youtube.com/watch?v=ePxY319YnFA">
              Learn More
            </Link>
          </div>
        </div>
      </div>
      <div className={clsx(styles.announcement, styles.announcementDark)}>
        <div className={styles.announcementInner}>
          Coming from v3? Check out our{' '}
          <Link to={useBaseUrl('/docs/guides/v4-migration')}>
            v3 to v4 migration guide
          </Link>
          .
        </div>
      </div>
      <main>
        {features && features.length > 0 && (
          <section className={styles.features}>
            <div className="container">
              <div className="row">
                {features.map((props, idx) => (
                  <Feature key={idx} {...props} />
                ))}
              </div>
            </div>
          </section>
        )}
      </main>
    </Layout>
  );
}

export default Home;
