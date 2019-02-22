# Contributing to AWE framework

:+1: :smile: First off, thanks for taking the time to contribute! :smile: :+1:

The following is a set of guidelines for contributing to AWE,
which is hosted in the [Almis Document Engine](https://gitlab.com/awe-team/awe) on GitLab.
These are mostly guidelines, not rules. Use your best judgment, and feel free to propose changes to
this document in a merge request.

## Issues
If writing a bug report, please make sure it has enough info. Include all relevant information.

If requesting a feature, understand that we appreciate the input! However, it may not immediately fit our roadmap, and it may take a while for us to get to your request.

## Gitflow
We use Gitflow as our branch management system. Please follow gitflow's guidelines while contributing to the project.

## Contributing guideline
- Please follow the repository's for all code and documentation.
- Create an issue and a merge request over the issue. Please fill the `features` or `bug` templates as best as possible.
- All feature branches should be based on `develop` and have the format `feature/branch_name`.
- Minor bug fixes, that is bug fixes that do not change, add, or remove any public API, should be based on `master` and have the format `hotfix/branch_name`.
- All merge requests should implement a single feature or fix a single bug. Merge Requests that involve multiple changes (it is our discretion what precisely this means) will be rejected with a reason.
- All commits should separated into logical units, i.e. unrelated changes should be in different commits within a pull request.
- Work over the new feature branch
- All new code must include unit tests. Bug fixes should have a test that fails previously and now passes. All new features should be covered. If your code does not have tests, or regresses old tests, it will be rejected.
- As you push over the project, the merge request pipeline will start. Please don't try to merge without passing all pipeline tasks
- When you finish your develop, remove the `WIP` status