# Work habits

<h4>Work habits</h4>
<p>

- Create issues at beginning of release cycle
- Add issues as they arise to document work
- Pair programming
- Mix of physical and digital meetings
- Create test units as classes are made
- When merging branches together, the involved developers approve the merge.

When starting on a new release, the first thing we do is to create issues and think about what we want to implement. Additionally, we add some issues while working, because we did not have them from the start. 

We mostly code in pairs as it is easier to avoid mistakes, and it gives us the benefit of having two points of view as we design the implementation. It is slightly less effective to code in pairs than to code individually, but we found that distributing work evenly and efficiently is difficult and errors are much more common when working individually than in pairs.

We use both physical and digital meetings, according to what is most efficient. When facing bigger obstacles, physical meetings are more useful. Digital meetings are more useful when solving easier problems. Digital meetings are also usually useful for work that takes quite a lot of time, because they are not as mentally draining as physical meetings.

Every group member strives after making test classes together with implementing the class or at least in reasonable time after a new class. The tests should not be made in a hurry at the end.

When merging branches together, the involved developers approve the merge. 
</p>


<h4>Workflow</h4>
<p> We have one official meeting on Thursdays, but when workload increases we increase the number of meetings as needed. Every developer also works with their assigned issue(s) in between group meetings. We assign new issues to developers that currently have no issues assigned once a week, usually on Thursday-meetings.
</p>


<h4>Code quality</h4>
<p> We use our own tests to make sure that new implemented functions do not cause errors to the already implemented code.
We use checkstyle, spotbugs and jacoco for testing and code revision. 
<strong>Spotbugs</strong> lets you see if there are any bugs in your code. 
<strong>Checkstyle</strong> will check if the code is written after a selected format(we use google java-format)  that can be changed for the code to look better. We try to follow most of the warnings, but we do not agree with all of them.
<strong>Jacoco</strong> lets you see the percentage of your methods that are tested.</p>


