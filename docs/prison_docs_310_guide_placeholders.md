
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison - Placeholder Guide

This document covers different aspects of placeholders within Prison.  It explains how they work, how to use them, and different ways to use them.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Overview

Placeholders allow sharing sharing data from one plugin, with another plugin, and without either plugin knowing anything about each other.

On the surface they appear to be simple, but there are a lot of moving parts below the surface, and with Prison Mines, there are even more things going on.

Add in to the mix, that different plugins deal with placeholders in slightly different ways, and you can windup with a challenge to them to work under different circumstances.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Placeholder Theory for Prison

There are two major types of placeholders in prison: Player based and Mine based.

The player based placeholders can only report on the player that is initiating the command, or request. These placeholders pertain to the player's attributes, such as rank or next rank.  Internally, all of these requests must include the player's UUID, which is why you cannot just add them to a sign, since the sign does not know any player's UUID.

The Mine based placeholders provide details about mines, and the mine name becomes part of the placeholder name.  Out of all of the possible mine based placeholders, each one is duplicated for each mine.  So, in rough terms, if there are different mine placeholders and you have 40 mines, then prison will generate about 400 place holders: 40 mines x 10 placeholders each = 400 placeholders for prison.

Prison has integrations for direct use of providing placeholder values to to the other plugins. Some of those other plugins request placeholder values using partial placeholder names.  Therefore to improve performance and to prevent having to always reconstructing the full placeholder names, prison precomputes the fragments for all placeholders.  Therefore, with our example of 40 mines and 10 placeholders, the actual internal number of placeholder combinations that prison will respond to is 800: 40 mines x 10 placeholders each x 2 for fragmented names = 800 placeholders for prison.

Off hand this may sound bad, but Prison utilizes enumerations for identifying placeholders, so they may be objects, but they are lightweight and helps ensure placeholders align with the code at compile time.  This not only provides better performance, and less memory consumption, but programming errors and typos are caught at compile time and not runtime, so they also provide for a more stable and reliable Prison environment.

Internally, placeholders within Prison are case insensitive.  But Prison uses lower cased placeholder names to register with any placeholder integration.  Therefore, although prison may be case insensitive, the placeholder plugins may not recognize the placeholders unless you use them as all lower case names.

Also, internally, prison only responds to the actual placeholder name, and the plugins generally strip off the place holder wrapper, such as curly braces { }, or percents % %.  So proper use of placeholders is dependent upon what is being required by your placeholder plugin.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Rank Command Placeholders

The Rank Commands recognize only two placeholders, but they are not considered part of the standard placeholders.  There are also only two placeholders that are recognized and both are case sensitive, and must also include curly braces too.

* {player}
* {player_uid}

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Placeholder Commands

*Since Prison v3.2.1-alpha.13*

There are a few commands within prison that will allow you list placeholders, search for placeholders, and to test random text that includes placeholders.

* **/prison placeholders**


* **/prison placeholders list**
* **/prison placeholders search**
* **/prison placeholders test**


* **/prison version** Provides the same placeholder listing as /prison placeholders list.



<h3>Prison Placeholder Command Listing</h3>

<img src="images/prison_docs_310_guide_placeholders_1.png" alt="Prison Placeholder Commands" title="Prison Placeholder Commands" width="500" />

Example of placeholder command listings


<h3>Prison Placeholder Listings</h3>

<img src="images/prison_docs_310_guide_placeholders_2.png" alt="Prison Placeholder Listing" title="Prison Placeholder Listing" width="500" />

Example of the list of placeholders that is available through **/prison version** and **/prison placeholders list**


<h3>Prison Placeholder Search with Two Search Patters</h3>

<img src="images/prison_docs_310_guide_placeholders_3.png" alt="Prison Placeholder Search" title="Prison Placeholder Search" width="500" />

This is an example of searching for placeholders using two search patterns: *temp5* and *format*. The term temp5 is the name of a mine and is an example of a the dynamic construction of placeholders, and that you can still perform a search with them.  The search patters can be any String fragment found in either the placeholder, or it's alias.  

If more than one search pattern is provided, then all patters must hit on the same placeholder to be included in the results.  They behave as a logical AND relationship. 



<h3>Prison Placeholder Listings - All Placeholders</h3>

<img src="images/prison_docs_310_guide_placeholders_4.png" alt="Prison Placeholder Listing" title="Prison Placeholder Listing" width="500" />

In this contrived example, since all placeholders begin with "prison", this search returns a listing of all placeholders. In this example, using the current Prison v3.2.1-alpha.13 release, it has generated 65 pages of results, at 6 placeholders per page which includes the alias.  


<h3>Prison Placeholder Listings</h3>

<img src="images/prison_docs_310_guide_placeholders_5.png" alt="Prison Placeholder Listing" title="Prison Placeholder Listing" width="500" />



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Chat Placeholders

There are two major ways a placeholder can be resolved within Prison: through chat or through a placeholder integration.

Chat based placeholders do not rely on other plugins for them to work.  Instead they use the org.bukkit.event.player.AsyncPlayerChatEvent, of which Prison will respond and provide translations to prison related placeholders that it find with the chat message.

Although no plugin is required for Prison to properly handle chat based placeholders, other plugins may be required to generate their use.  Such as EssentialsX's Chat plugin.  It provides a way to prefix chat messages with placeholders.



# Enabling EssentialX's Chat Placeholders

Set up the EssentialX's Chat plugin: [Setting up EssentialsX](prison_docs_0xx_setting_up_EssentialsX.md).

Enabling the chat placeholder just requires editing one line within the `config.yml` file.  Search for the keyword **EssentialsChat** in that file, then edit the `format:` tag.  For example:

    format: '<{prison_rank_tag}:{DISPLAYNAME}>{MESSAGE}'

Once setup, restart the server. Or use **/essentials reload**.  Do not use force all the plugins to be reloaded with a tool such as plugman since Prison (and other plugins) may fail to re-load properly.



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Enabling HolographicDisplays Placeholders

 (will be added shortly...)



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

