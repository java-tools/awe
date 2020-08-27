const versions = require('./versions.json');

module.exports = {
	title: 'Awe framework',
	tagline: 'Low coding complete functional web applications',
	url: 'https://docs.aweframework.com',
	baseUrl: '/',
	onBrokenLinks: 'throw',
	favicon: 'icon/favicon.ico',
	organizationName: 'aweframework',
	projectName: 'awe',
	themeConfig: {
		navbar: {
			title: 'Awe',
			logo: {
				alt: 'Awe framework Logo',
				src: 'img/logo.svg',
				// srcDark: 'img/logo white.svg'
			},
			hideOnScroll: true,
			items: [
				{
					type: 'docsVersionDropdown',
					position: 'left',
				},
				{
					to: 'blog',
					label: 'Blog',
					position: 'left'
				},
				{
					href: 'https://forum.aweframework.com/',
					to: 'community',
					label: 'Forum', position: 'left'
				},
				{
					alt: 'Gitlab repository',
					href: 'https://gitlab.com/aweframework/awe',
          className: 'header-gitlab-link',
					position: 'right'
				},
			],
		},
		footer: {
			style: 'dark',
			links: [
				{
					title: 'Docs',
					items: [
						{
							label: 'Introduction',
							to: 'docs/',
						},
						{
							label: 'Migration from v3 to v4',
							to: 'docs/guides/v4-migration',
						},
					],
				},
				{
					title: 'Community',
					items: [
						{
							label: 'Stack Overflow',
							href: 'https://stackoverflow.com/questions/tagged/awe',
						},
						{
							label: 'Forum',
							href: 'https://forum.aweframework.com/',
						},
					],
				},
				{
					title: 'More',
					items: [
						{
							label: 'Gitlab repository',
							href: 'https://gitlab.com/aweframework/awe',
						},
						{
							label: 'Javadoc',
							href: 'https://aweframework.gitlab.io/awe/javadoc-api/index.html',
						}
					],
				},
			],
			copyright: `Copyright © ${new Date().getFullYear()} Awe framework, Almis Informática S.L.`,
		},
		algolia: {
			apiKey: 'bbb756b741640f975ac0158bcedcefcb',
			indexName: 'aweframework_awe',
			searchParameters: {
				facetFilters: [`version:${versions[0]}`],
			},
		},
	},
	presets: [
		[
			'@docusaurus/preset-classic',
			{
				docs: {
					homePageId: 'intro',
					sidebarPath: require.resolve('./sidebars.js'),
					showLastUpdateAuthor: true,
					showLastUpdateTime: true,
					editUrl:
						'https://gitlab.com/aweframework/awe/edit/develop/website/',
				},
				blog: {
					showReadingTime: true,
					editUrl:
						'https://gitlab.com/aweframework/awe/edit/develop/website/',
					postsPerPage: 3,
					feedOptions: {
						type: 'all',
						language: 'es',
						copyright: `Copyright © ${new Date().getFullYear()} Almis, Inc.`,
					},
				},
				theme: {
					customCss: require.resolve('./src/css/custom.css'),
				},
			},
		],
	],
};
