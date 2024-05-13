# ABM: How do natural disasters impact the spread of diseases?

&emsp; This project aims to explore the impact of natural disasters on the spread of diseases, specifically COVID-19, through an agent-based model (ABM). Our model addresses the lack of research regarding "compound risks" arising from the co-occurrence of COVID-19 and natural disasters. Utilizing the compartmental 3-state SIR(susceptible, infected, recovered) epidemic model, the ABM incorporates hospital capacity as a proxy variable for natural disasters. The model's structural and design choices are primarily based on principles of ABM (Perez et al., 2009), previous research on the interactions between disease and natural disasters (de Vries et al., 2021), as well as statistics on the spread of COVID-19 (Kucharski et al., 2020).

&emsp; Our simulation involves interactions among agents, where susceptible agents have a chance of becoming infected. This probability is determined by a pseudo density or the number of agents within a specified radius around the susceptible agent. Infected agents can recover within the hospital (a default value of 2 days) or naturally (a default value of 11 days) We conduct virtual experiments to explore the robustness of our model by adjusting various parameters, including initial infected agents, total population, and proportion of hospital recovery time to natural recovery times (effectiveness of the hospital itself).

&emsp; Due to natural disasters reducing hospital capacity as resources are redirected to crisis management, our principal hypothesis predicts that the presence of natural disasters hinders the population's ability to effectively combat the spread of the disease. Additionally, specific hypotheses are employed to assess the effect of previously described adjustments: the impact of increasing the amount of initially infected agents on disease spread, the model's robustness to population scaling/ceilings, and the influence of recovery time proportions on infection patterns.

&emsp; Overall, our study contributes to understanding how natural disasters affect disease propagation and offers insights into optimal hospital planning amid such crises as it underscores the importance of considering compound risks in public health preparedness and decision-making.

# Additional Resources
[Extensive Literature Review and Methodologies](https://docs.google.com/document/d/1HDjQmd92sRcfjmWrNkk_tsjCDY04mwYDOTWcgefKe3k/edit?usp=sharing)

## Conference Poster
![Alt text](https://github.com/maxxhvo/ABM-COVID-19_naturaldisaster/blob/main/ABM_CONFERENCE.jpg?raw=true)





