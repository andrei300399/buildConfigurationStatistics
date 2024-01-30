from git import Repo
import re
repo = Repo('C:\\frontmavemplugversion\\frontend-maven-plugin')

commits = list(repo.iter_commits('master'))
commit_first = commits[0]

i = 1
for commit in commits[1:]:
    diff_changes = repo.git.diff("--shortstat", commit_first, commit)
    list_changes = re.findall(r'\d+', diff_changes)
    if i == 5:
        diff_changes = repo.git.diff(commit_first, commit)
    if len(list_changes) < 2:
        continue
    if len(list_changes) > 2:
        list_changes_count = int(list_changes[1]) + int(list_changes[2])
    else:
        list_changes_count = int(list_changes[1])

    if list_changes_count > 500:
        print(i)
        print("500+: ", diff_changes, commit)
        repo.git.reset(commit)
        new_branch_name = 'reverse_branch' + str(i)
        repo.git.branch(new_branch_name)

        repo.git.checkout(new_branch_name)
        repo.git.push('origin', new_branch_name)
        commit_first = commit
        i+=1
        if i > 12:
            break
   