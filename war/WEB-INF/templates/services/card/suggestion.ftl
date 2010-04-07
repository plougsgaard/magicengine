<ul>
	<#if model.cardSuggestions??>
		<#list model.cardSuggestions as card>
			<li>${card.cardName}</li>
		</#list>
	<#else>
		<!-- Do nothing -->
	</#if>
</ul>
